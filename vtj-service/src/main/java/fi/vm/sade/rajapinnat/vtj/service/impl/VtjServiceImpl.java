package fi.vm.sade.rajapinnat.vtj.service.impl;

import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.api.Huollettava;
import fi.vm.sade.rajapinnat.vtj.api.Huoltaja;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo.EntinenNimiTyyppi;
import fi.vm.sade.rajapinnat.vtj.PassivoituException;
import fi.vm.sade.rajapinnat.vtj.service.VtjService;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Kansalaisuus;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.StringUtils;
import org.tempuri.SoSoSoap;
import org.tempuri.TeeHenkilonTunnusKyselyResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: tommiha
 * Date: 6/26/13
 * Time: 2:23 PM
 */

public class VtjServiceImpl implements VtjService {

    private static Logger logger = LoggerFactory.getLogger(VtjServiceImpl.class);

    private SoSoSoap soSoSoap;
    private String kayttajatunnus;
    private String salasana;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Cacheable(value = "vtj", key = "#hetu")
    public YksiloityHenkilo teeHenkiloKysely(String loppukayttaja, String hetu, boolean logMessage) {
        VTJHenkiloVastaussanoma vastaus = getVtjHenkiloVastaussanoma(loppukayttaja, hetu, false, logMessage);
        return convert(vastaus);
    }

    public VTJHenkiloVastaussanoma getVtjHenkiloVastaussanoma(String loppukayttaja, String hetu, boolean retried, boolean logMessage) {
        TeeHenkilonTunnusKyselyResponse.TeeHenkilonTunnusKyselyResult tunnusKyselyResult = soSoSoap.teeHenkilonTunnusKysely("OPHREK", kayttajatunnus, salasana, loppukayttaja, null, hetu, null, null, null, null, null, null, null);
        VTJHenkiloVastaussanoma vastaus = (VTJHenkiloVastaussanoma) tunnusKyselyResult.getContent().get(0);

        if (logMessage) {
            try {
                logger.info("Response from VTJ for hetu '{}': {}", hetu, objectMapper.writeValueAsString(vastaus));
            } catch (IOException e) {
                logger.error("Couldn't log received message", e);
            }
        }

        if (vastaus == null) {
            throw new NotFoundException("Invalid response from VTJ");
        }

        // paluukoodit: https://github.com/Opetushallitus/rajapinnat/blob/8e1faa038a61d67a4e98c4897bc9013aa218f81a/vtj/vtj-remote-api/src/main/resources/wsdl/VTJHenkilotiedotKatalogi.xsd#L608-L643
        String paluuKoodi = vastaus.getPaluukoodi() != null ? vastaus.getPaluukoodi().getKoodi() : null;

        switch (paluuKoodi) {
            case "0000":
            case "0018":
                return vastaus;
            case "0001":
            case "0006":
                throw new NotFoundException("Could not find person.");
            case "0002":
                // tarkistetaan onko henkilön hetu muuttunut
                String uusiHetu = (vastaus.getHenkilo() != null && vastaus.getHenkilo().getHenkilotunnus() != null) ?
                        vastaus.getHenkilo().getHenkilotunnus().getValue() : null;
                if (uusiHetu != null && !uusiHetu.equals(hetu)) {
                    if(retried) {
                        // todennäköisesti virhe datassa, lopeta rekursio
                        throw new NotFoundException("Query with a new active hetu should not return another new active hetu.");
                    }

                    logger.info("Hetu has changed for a person. Old: " + hetu + ", new: " + uusiHetu);
                    // haetaan tiedot uudestaan uudella hetulla
                    return getVtjHenkiloVastaussanoma(loppukayttaja, uusiHetu, true, logMessage);
                }
                throw new PassivoituException();
            default:
                logger.warn("Unknown response code {} for hetu '{}'", paluuKoodi, hetu);
                // taaksepäin yhteensopivuuden vuoksi heitetään NotFoundException
                throw new NotFoundException("Unknown response code '" + paluuKoodi +"'.");
        }
    }

    private YksiloityHenkilo convert(VTJHenkiloVastaussanoma vastaus) {
        
        YksiloityHenkilo henkilo = new YksiloityHenkilo();
        Henkilo vtjHenkilo = vastaus.getHenkilo();
        assert(vtjHenkilo != null);

        henkilo.setEtunimi(vtjHenkilo.getNykyisetEtunimet().getEtunimet());
        henkilo.setSukunimi(vtjHenkilo.getNykyinenSukunimi().getSukunimi());
        if(!vtjHenkilo.getNykyisetEtunimet().equals(vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi()) &&
                !vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi().trim().contains(" ")) {
            henkilo.setKutsumanimi(vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi());
        }
        if (vtjHenkilo.getEntinenNimi() != null) {
            List<YksiloityHenkilo.EntinenNimi> entisetNimet = new ArrayList<>();
            for (Henkilo.EntinenNimi entinenNimi : vtjHenkilo.getEntinenNimi()) {
                EntinenNimiTyyppi tyyppi = EntinenNimiTyyppi.getByKoodi(entinenNimi.getNimilajikoodi());
                entisetNimet.add(new YksiloityHenkilo.EntinenNimi(tyyppi, entinenNimi.getNimi()));
            }
            henkilo.setEntisetNimet(entisetNimet);
        }

        String turvakieltoTieto = vtjHenkilo.getTurvakielto().getTurvakieltoTieto();
        henkilo.setTurvakielto("1".equals(turvakieltoTieto));

        henkilo.setHetu(vtjHenkilo.getHenkilotunnus().getValue());

        henkilo.setSukupuoli(vtjHenkilo.getSukupuoli().getSukupuolikoodi());
        
        henkilo.setAidinkieliKoodi(KielikoodiKludge.korjaaVirheellinenKoodi(vtjHenkilo.getAidinkieli().getKielikoodi()));
        
        if (vtjHenkilo.getSahkopostiosoite() != null) {
            henkilo.setSahkoposti(vtjHenkilo.getSahkopostiosoite());
        }
        
        if (vtjHenkilo.getKansalaisuus() != null) {
            for (Kansalaisuus vtjKansalaisuus : vtjHenkilo.getKansalaisuus()) {
                henkilo.addKansalaisuusKoodi(KansallisuusKludge.korjaaVirheellinenKoodi(vtjKansalaisuus.getKansalaisuuskoodi3()));
            }
        }
        
        if (vtjHenkilo.getVakinainenKotimainenOsoite() != null) {
            StringBuffer postiOsoiteS = new StringBuffer();
            StringBuffer postiOsoiteR = new StringBuffer();

            String huoneistonumero = StringUtils.trimLeadingCharacter(vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero(), '0');

            if (vtjHenkilo.getVakinainenKotimainenOsoite().getKatuS() != null) {
                postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatuS());
                postiOsoiteS.append(" ");
                postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatunumero());
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain() != null) {
                    postiOsoiteS.append(" ");
                    postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain());
                }
                if (StringUtils.hasLength(huoneistonumero)) {
                    postiOsoiteS.append(" ");
                    postiOsoiteS.append(huoneistonumero);
                }
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain() != null) {
                    postiOsoiteS.append(" ");
                    postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain());
                }
            }
            
            if (vtjHenkilo.getVakinainenKotimainenOsoite().getKatuR() != null) {
                postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatuS());
                postiOsoiteR.append(" ");
                postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatunumero());
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain() != null) {
                    postiOsoiteR.append(" ");
                    postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain());
                }
                if (StringUtils.hasLength(huoneistonumero)) {
                    postiOsoiteR.append(" ");
                    postiOsoiteR.append(huoneistonumero);
                }
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain() != null) {
                    postiOsoiteR.append(" ");
                    postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain());
                }
            }
            
            YksiloityHenkilo.OsoiteTieto kotimaanOsoite = new YksiloityHenkilo.OsoiteTieto(
                    "yhteystietotyyppi4",
                    postiOsoiteS.toString().trim(),
                    postiOsoiteR.toString().trim(),
                    vtjHenkilo.getVakinainenKotimainenOsoite().getPostinumero(),
                    vtjHenkilo.getVakinainenKotimainenOsoite().getPostitoimipaikkaS(),
                    vtjHenkilo.getVakinainenKotimainenOsoite().getPostitoimipaikkaR(),
                    "Suomi",
                    "Finland");
            henkilo.addOsoiteTieto(kotimaanOsoite);
        }
        
        if (vtjHenkilo.getVakinainenUlkomainenOsoite() != null) {
            YksiloityHenkilo.OsoiteTieto ulkomaanOsoite = new YksiloityHenkilo.OsoiteTieto(
                    "yhteystietotyyppi5",
                    vtjHenkilo.getVakinainenUlkomainenOsoite().getUlkomainenLahiosoite(),
                    null,
                    null,
                    vtjHenkilo.getVakinainenUlkomainenOsoite().getUlkomainenPaikkakunta(),
                    null,
                    vtjHenkilo.getVakinainenUlkomainenOsoite().getValtioS(),
                    vtjHenkilo.getVakinainenUlkomainenOsoite().getValtioR());
            henkilo.addOsoiteTieto(ulkomaanOsoite);
        }

        if (vtjHenkilo.getKotikunta() != null) {
            henkilo.setKotikunta(vtjHenkilo.getKotikunta().getKuntanumero());
        }

        if (vtjHenkilo.getHuoltaja() != null) {
            List<Huoltaja> huoltajat = vtjHenkilo.getHuoltaja().stream()
                    .map(vtjHuoltaja -> new Huoltaja(
                            vtjHuoltaja.getNykyisetEtunimet().getEtunimet(),
                            vtjHuoltaja.getNykyinenSukunimi().getSukunimi(),
                            vtjHuoltaja.getHenkilotunnus(),
                            vtjHuoltaja.getHuoltotiedot().getHenkilosuhdelajikoodi()))
                    .filter(huoltaja -> StringUtils.hasLength(huoltaja.getHuoltajuustyyppiKoodi()))
                    .collect(Collectors.toList());
            henkilo.setHuoltajat(huoltajat);
        }

        List<Huollettava> huollettavat = vtjHenkilo.getHuollettava().stream()
            .filter(vtjHuollettava -> StringUtils.hasText(vtjHuollettava.getHenkilotunnus()))
            .map(vtjHuollettava -> new Huollettava(vtjHuollettava.getEtunimet(), vtjHuollettava.getSukunimi(), vtjHuollettava.getHenkilotunnus()))
            .collect(Collectors.toList());
        henkilo.setHuollettavat(huollettavat);

        return henkilo;
    }

    public SoSoSoap getSoSoSoap() {
        return soSoSoap;
    }

    public void setSoSoSoap(SoSoSoap soSoSoap) {
        this.soSoSoap = soSoSoap;
    }

    public String getKayttajatunnus() {
        return kayttajatunnus;
    }

    public void setKayttajatunnus(String kayttajatunnus) {
        this.kayttajatunnus = kayttajatunnus;
    }

    public String getSalasana() {
        return salasana;
    }

    public void setSalasana(String salasana) {
        this.salasana = salasana;
    }
}

package fi.vm.sade.rajapinnat.vtj.service.impl;

import org.springframework.cache.annotation.Cacheable;
import org.tempuri.SoSoSoap;
import org.tempuri.TeeHenkilonTunnusKyselyResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.service.VtjService;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Kansalaisuus;

/**
 * User: tommiha
 * Date: 6/26/13
 * Time: 2:23 PM
 */
public class VtjServiceImpl implements VtjService {

    private SoSoSoap soSoSoap;
    private String kayttajatunnus;
    private String salasana;
    private static Logger logger = LoggerFactory.getLogger(VtjServiceImpl.class);
    
    @Cacheable(value = "vtj", key = "#hetu")
    public YksiloityHenkilo teeHenkiloKysely(String loppukayttaja, String hetu, boolean logMessage) {
        VTJHenkiloVastaussanoma vastaus = getVtjHenkiloVastaussanoma(loppukayttaja, hetu);
        return convert(vastaus, logMessage);
    }

    private VTJHenkiloVastaussanoma getVtjHenkiloVastaussanoma(String loppukayttaja, String hetu) {
        TeeHenkilonTunnusKyselyResponse.TeeHenkilonTunnusKyselyResult tunnusKyselyResult = soSoSoap.teeHenkilonTunnusKysely("OPHREK", kayttajatunnus, salasana, loppukayttaja, null, hetu, null, null, null, null, null, null, null);
        VTJHenkiloVastaussanoma vastaus = (VTJHenkiloVastaussanoma) tunnusKyselyResult.getContent().get(0);
        if("0001".equals(vastaus.getPaluukoodi().getKoodi())) {
            throw new NotFoundException("Could not find person.");
        }
        return vastaus;
    }

    private YksiloityHenkilo convert(VTJHenkiloVastaussanoma vastaus, boolean logMessage) {
        if (logMessage) {
            logger.error("\nDEBUG LOG - VTJHenkiloVastaussanoma:\n" + vastaus.toString());
        }
        
        YksiloityHenkilo henkilo = new YksiloityHenkilo();
        Henkilo vtjHenkilo = vastaus.getHenkilo();
        
        henkilo.setEtunimi(vtjHenkilo.getNykyisetEtunimet().getEtunimet());
        henkilo.setSukunimi(vtjHenkilo.getNykyinenSukunimi().getSukunimi());
        henkilo.setKutsumanimi(vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi());

        String turvakieltoTieto = vtjHenkilo.getTurvakielto().getTurvakieltoTieto();
        henkilo.setTurvakielto(turvakieltoTieto.equals("1") ? true : false);
        henkilo.setHetu(vtjHenkilo.getHenkilotunnus().getValue());

        String sukupuolikoodi = vtjHenkilo.getSukupuoli().getSukupuolikoodi();
        henkilo.setSukupuoli(sukupuolikoodi.equals("2") ? YksiloityHenkilo.Sukupuoli.NAINEN : YksiloityHenkilo.Sukupuoli.MIES);
        
        henkilo.setAidinkieliKoodi(vtjHenkilo.getAidinkieli().getKielikoodi());
        
        if (vtjHenkilo.getSahkopostiosoite() != null) {
            henkilo.setSahkoposti(vtjHenkilo.getSahkopostiosoite());
        }
        
        if (vtjHenkilo.getKansalaisuus() != null) {
            for (Kansalaisuus vtjKansalaisuus : vtjHenkilo.getKansalaisuus()) {
                henkilo.addKansalaisuusKoodi(vtjKansalaisuus.getKansalaisuuskoodi3());
            }
        }
        
        if (vtjHenkilo.getVakinainenKotimainenOsoite() != null) {
            StringBuffer postiOsoiteS = new StringBuffer();
            StringBuffer postiOsoiteR = new StringBuffer();
            
            if (vtjHenkilo.getVakinainenKotimainenOsoite().getKatuS() != null) {
                postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatuS());
                postiOsoiteS.append(" ");
                postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getKatunumero());
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain() != null) {
                    postiOsoiteS.append(" ");
                    postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain());
                }
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero() != null) {
                    postiOsoiteS.append(" ");
                    postiOsoiteS.append(vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero());
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
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero() != null) {
                    postiOsoiteR.append(" ");
                    postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero());
                }
                if (vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain() != null) {
                    postiOsoiteR.append(" ");
                    postiOsoiteR.append(vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain());
                }
            }
            
            YksiloityHenkilo.OsoiteTieto kotimaanOsoite = henkilo.new OsoiteTieto(
                    "yhteystietotyyppi4",
                    postiOsoiteS.toString(),
                    postiOsoiteR.toString(),
                    vtjHenkilo.getVakinainenKotimainenOsoite().getPostinumero(),
                    vtjHenkilo.getVakinainenKotimainenOsoite().getPostitoimipaikkaS(),
                    vtjHenkilo.getVakinainenKotimainenOsoite().getPostitoimipaikkaR(),
                    "Suomi",
                    "Finland");
            henkilo.addOsoiteTieto(kotimaanOsoite);
        }
        
        if (vtjHenkilo.getVakinainenUlkomainenOsoite() != null) {
            YksiloityHenkilo.OsoiteTieto ulkomaanOsoite = henkilo.new OsoiteTieto(
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

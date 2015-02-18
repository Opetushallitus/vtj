package fi.vm.sade.rajapinnat.vtj.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.tempuri.SoSoSoap;
import org.tempuri.TeeHenkilonTunnusKyselyResponse;

import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.service.VtjService;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Edunvalvonta.HenkiloEdunvalvoja;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Edunvalvonta.OikeusaputoimistoEdunvalvoja;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Edunvalvonta.YritysJaYhteisoEdunvalvoja;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Edunvalvontavaltuutus;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Edunvalvontavaltuutus.HenkiloEdunvalvontavaltuutettu;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.EntinenNimi;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Huoltaja;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.Kansalaisuus;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.KotimainenPostiosoite;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.TilapainenKotimainenOsoite;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.TilapainenUlkomainenOsoite;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.UlkomainenHenkilonumero;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma.Henkilo.UlkomainenPostiosoite;

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
        
        YksiloityHenkilo henkilo = new YksiloityHenkilo();
        Henkilo vtjHenkilo = vastaus.getHenkilo();
        
        if (logMessage) {
            logVTJMessage(vtjHenkilo);
        }
        
        henkilo.setEtunimi(vtjHenkilo.getNykyisetEtunimet().getEtunimet());
        henkilo.setSukunimi(vtjHenkilo.getNykyinenSukunimi().getSukunimi());
        if(!vtjHenkilo.getNykyisetEtunimet().equals(vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi()) &&
                !vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi().trim().contains(" ")) {
            henkilo.setKutsumanimi(vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi());
        }

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
    
    private void logVTJMessage(Henkilo vtjHenkilo) {
        StringBuffer sb = new StringBuffer();
        sb.append("\nDEBUG LOG - VTJ vastaussanoman tiedot");
        sb.append("\nNykyinenSukunimi: " + vtjHenkilo.getNykyinenSukunimi().getSukunimi());
        sb.append("\nNykyisetEtunimet: " + vtjHenkilo.getNykyisetEtunimet().getEtunimet());
        sb.append("\nNykyinenKutsumanimi: " + vtjHenkilo.getNykyinenKutsumanimi().getKutsumanimi());
        sb.append("\nHenkilotunnus: " + vtjHenkilo.getHenkilotunnus().getValue());
        if (vtjHenkilo.getEntinenNimi() != null) {
            sb.append("\nEntisetNimet[]:");
            for (EntinenNimi en : vtjHenkilo.getEntinenNimi()) {
                sb.append("\n--EntinenNimi: ");
                sb.append("\n----Alkupvm: " + en.getAlkupvm());
                sb.append("\n----Loppupvm: " + en.getLoppupvm());
                sb.append("\n----Nimi: " + en.getNimi());
                sb.append("\n----Nimilajikoodi: " + en.getNimilajikoodi());
            }
        }
        sb.append("\nHuoltajaLkm: " + vtjHenkilo.getHuoltajaLkm());
        if (vtjHenkilo.getSahkopostiosoite() != null) {
            sb.append("\nSahkopostiosoite: " + vtjHenkilo.getSahkopostiosoite());
        }
        sb.append("\nAidinkieli::Kielikoodi: " + vtjHenkilo.getAidinkieli().getKielikoodi());
        if (vtjHenkilo.getAmmatti() != null) {
            sb.append("\nAmmatti::AmmattiNimi: " + vtjHenkilo.getAmmatti().getAmmattiNimi());
        }
        if (vtjHenkilo.getEdunvalvonta() != null) {
            sb.append("\nEdunvalvonta::Alkupvm: " + vtjHenkilo.getEdunvalvonta().getAlkupvm());
            sb.append("\nEdunvalvonta::Paattymispvm: " + vtjHenkilo.getEdunvalvonta().getPaattymispvm());
            sb.append("\nEdunvalvonta::Rajoituskoodi: " + vtjHenkilo.getEdunvalvonta().getRajoituskoodi());
            sb.append("\nEdunvalvonta::Tehtavienjakokoodi: " + vtjHenkilo.getEdunvalvonta().getTehtavienjakokoodi());
            if (vtjHenkilo.getEdunvalvonta().getHenkiloEdunvalvoja() != null) {
                sb.append("\nEdunvalvonta::HenkiloEdunvalvojat[]:");
                for (HenkiloEdunvalvoja ev: vtjHenkilo.getEdunvalvonta().getHenkiloEdunvalvoja()) {
                    sb.append("\n--HenkiloEdunvalvoja:");
                    sb.append("\n----Henkilotunnus: " + ev.getHenkilotunnus());
                    sb.append("\n----Syntymaaika: " + ev.getSyntymaaika());
                    sb.append("\n----TehtavaAlkupvm: " + ev.getTehtavaAlkupvm());
                    sb.append("\n----TehtavaLoppupvm: " + ev.getTehtavaLoppupvm());
                    sb.append("\n----NykyinenSukunimi: " + ev.getNykyinenSukunimi().getSukunimi());
                    sb.append("\n----NykyisetEtunimet: " + ev.getNykyisetEtunimet().getEtunimet());
                }
            }
            if (vtjHenkilo.getEdunvalvonta().getOikeusaputoimistoEdunvalvoja() != null) {
                sb.append("\nEdunvalvonta::OikeusaputoimistoEdunvalvojat[]:");
                for (OikeusaputoimistoEdunvalvoja oev : vtjHenkilo.getEdunvalvonta().getOikeusaputoimistoEdunvalvoja()) {
                    sb.append("\n--OikeusaputoimistoEdunvalvoja:");
                    sb.append("\n----TehtavaAlkupvm: " + oev.getTehtavaAlkupvm());
                    sb.append("\n----TehtavaLoppupvm: " + oev.getTehtavaLoppupvm());
                    sb.append("\n----ViranomainenS: " + oev.getViranomainenS());
                    sb.append("\n----ViranomainenR: " + oev.getViranomainenR());
                    sb.append("\n----Viranomaiskoodi: " + oev.getViranomaiskoodi());
                }
            }
            if (vtjHenkilo.getEdunvalvonta().getYritysJaYhteisoEdunvalvoja() != null) {
                sb.append("\nEdunvalvonta::YritysJaYhteisoEdunvalvojat[]:");
                for (YritysJaYhteisoEdunvalvoja yyev : vtjHenkilo.getEdunvalvonta().getYritysJaYhteisoEdunvalvoja()) {
                    sb.append("\n--YritysJaYhteisoEdunvalvoja:");
                    sb.append("\n----TehtavaAlkupvm: " + yyev.getTehtavaAlkupvm());
                    sb.append("\n----TehtavaLoppupvm: " + yyev.getTehtavaLoppupvm());
                    sb.append("\n----Nimi: " + yyev.getNimi());
                    sb.append("\n----Ytunnus: " + yyev.getYtunnus());
                }
            }
            if (vtjHenkilo.getEdunvalvontavaltuutus() != null) {
                sb.append("\nEdunvalvonta::Edunvalvontavaltuutukset[]:");
                for (Edunvalvontavaltuutus evv : vtjHenkilo.getEdunvalvontavaltuutus()) {
                    sb.append("\n--Edunvalvontavaltuutus:");
                    sb.append("\n----Alkupvm: " + evv.getAlkupvm());
                    sb.append("\n----Paattymispvm: " + evv.getPaattymispvm());
                    sb.append("\n----Tehtavienjakokoodi: " + evv.getTehtavienjakokoodi());
                    sb.append("\n----HenkiloEdunvalvontavaltuutetut[]:");
                    for (HenkiloEdunvalvontavaltuutettu hevv : evv.getHenkiloEdunvalvontavaltuutettu()) {
                        sb.append("\n------HenkiloEdunvalvontavaltuutettu:");
                        sb.append("\n--------Henkilotunnus: " + hevv.getHenkilotunnus());
                        sb.append("\n--------Syntymaaika: " + hevv.getSyntymaaika());
                        sb.append("\n--------ValtuutusAlkupvm: " + hevv.getValtuutusAlkupvm());
                        sb.append("\n--------ValtuutusLoppupvm: " + hevv.getValtuutusLoppupvm());
                        sb.append("\n--------NykyinenSukunimi: " + hevv.getNykyinenSukunimi().getSukunimi());
                        sb.append("\n--------NykyisetEtunimet: " + hevv.getNykyisetEtunimet().getEtunimet());
                    }
                }
            }
        }
        if (vtjHenkilo.getHuoltaja() != null) {
            sb.append("\nHuoltajat[]:");
            for (Huoltaja h : vtjHenkilo.getHuoltaja()) {
                sb.append("\n--Huoltaja:");
                sb.append("\n----Henkilotunnus: " + h.getHenkilotunnus());
                sb.append("\n----Syntymaaika: " + h.getSyntymaaika());
                sb.append("\n----Huoltotiedot::Henkilosuhdelajikoodi: " + h.getHuoltotiedot().getHenkilosuhdelajikoodi());
                sb.append("\n----NykyinenSukunimi: " + h.getNykyinenSukunimi().getSukunimi());
                sb.append("\n----NykyisetEtunimet: " + h.getNykyisetEtunimet().getEtunimet());
                if (h.getVakinainenKotimainenOsoite() != null) {
                    sb.append("\n----VakinainenKotimainenOsoite:");
                    sb.append("\n------AsuminenAlkupvm: " + h.getVakinainenKotimainenOsoite().getAsuminenAlkupvm());
                    sb.append("\n------AsuminenLoppupvm: " + h.getVakinainenKotimainenOsoite().getAsuminenLoppupvm());
                    sb.append("\n------Huoneistonumero: " + h.getVakinainenKotimainenOsoite().getHuoneistonumero());
                    sb.append("\n------Jakokirjain: " + h.getVakinainenKotimainenOsoite().getJakokirjain());
                    sb.append("\n------Katunumero: " + h.getVakinainenKotimainenOsoite().getKatunumero());
                    sb.append("\n------KatuS: " + h.getVakinainenKotimainenOsoite().getKatuS());
                    sb.append("\n------KatuR: " + h.getVakinainenKotimainenOsoite().getKatuR());
                    sb.append("\n------Porraskirjain: " + h.getVakinainenKotimainenOsoite().getPorraskirjain());
                    sb.append("\n------Postinumero: " + h.getVakinainenKotimainenOsoite().getPostinumero());
                    sb.append("\n------PostitoimipaikkaS: " + h.getVakinainenKotimainenOsoite().getPostitoimipaikkaS());
                    sb.append("\n------PostitoimipaikkaR: " + h.getVakinainenKotimainenOsoite().getPostitoimipaikkaR());
                }
                if (h.getVakinainenUlkomainenOsoite() != null) {
                    sb.append("\n----VakinainenUlkomainenOsoite:");
                    sb.append("\n------AsuminenAlkupvm: " + h.getVakinainenUlkomainenOsoite().getAsuminenAlkupvm());
                    sb.append("\n------AsuminenLoppupvm: " + h.getVakinainenUlkomainenOsoite().getAsuminenLoppupvm());
                    sb.append("\n------UlkomainenLahiosoite: " + h.getVakinainenUlkomainenOsoite().getUlkomainenLahiosoite());
                    sb.append("\n------UlkomainenPaikkakunta: " + h.getVakinainenUlkomainenOsoite().getUlkomainenPaikkakunta());
                    sb.append("\n------Valtiokoodi3: " + h.getVakinainenUlkomainenOsoite().getValtiokoodi3());
                    sb.append("\n------ValtioS: " + h.getVakinainenUlkomainenOsoite().getValtioS());
                    sb.append("\n------ValtioR: " + h.getVakinainenUlkomainenOsoite().getValtioR());
                    sb.append("\n------ValtioSelvakielinen: " + h.getVakinainenUlkomainenOsoite().getValtioSelvakielinen());
                }
            }
        }
        sb.append("\nKansalaisuudet[]:");
        for (Kansalaisuus k : vtjHenkilo.getKansalaisuus()) {
            sb.append("\n--Kansalaisuus:");
            sb.append("\n----Kansalaisuuskoodi3: " + k.getKansalaisuuskoodi3());
            sb.append("\n----KansalaisuusS: " + k.getKansalaisuusS());
            sb.append("\n----KansalaisuusR: " + k.getKansalaisuusR());
            sb.append("\n----KansalaisuusSelvakielinen: " + k.getKansalaisuusSelvakielinen());
            sb.append("\n----Saamispvm: " + k.getSaamispvm());
        }
        sb.append("\nKotikunta::KuntaS: " + vtjHenkilo.getKotikunta().getKuntaS());
        sb.append("\nKotikunta::KuntaR: " + vtjHenkilo.getKotikunta().getKuntaR());
        sb.append("\nKotikunta::Kuntanumero: " + vtjHenkilo.getKotikunta().getKuntanumero());
        sb.append("\nKotikunta::KuntasuhdeAlkupvm: " + vtjHenkilo.getKotikunta().getKuntasuhdeAlkupvm());
        if (vtjHenkilo.getKotimainenPostiosoite() != null) {
            sb.append("\nKotimainsetPostiosoitteet[]:");
            for (KotimainenPostiosoite kpo : vtjHenkilo.getKotimainenPostiosoite()) {
                sb.append("\n--KotimainenPostiosoite:");
                sb.append("\n----ParasOsoiteTieto: " + kpo.getParasOsoiteTieto());
                sb.append("\n----Postinumero: " + kpo.getPostinumero());
                sb.append("\n----PostiosoiteAlkupvm: " + kpo.getPostiosoiteAlkupvm());
                sb.append("\n----PostiosoiteLoppupvm: " + kpo.getPostiosoiteLoppupvm());
                sb.append("\n----PostiosoiteS: " + kpo.getPostiosoiteS());
                sb.append("\n----PostiosoiteR: " + kpo.getPostiosoiteR());
                sb.append("\n----PostitoimipaikkaS: " + kpo.getPostitoimipaikkaS());
                sb.append("\n----PostitoimipaikkaR: " + kpo.getPostitoimipaikkaR());
            }
        }
        if (vtjHenkilo.getKuolintiedot().getKuolinpvm() != null) {
            sb.append("\nKuolintiedot::Kuolinpvm: " + vtjHenkilo.getKuolintiedot().getKuolinpvm());
        }
        if (vtjHenkilo.getTilapainenKotimainenOsoite() != null) {
            sb.append("\nTilapaisetKotimaisetOsoitteet[]:");
            for (TilapainenKotimainenOsoite tko : vtjHenkilo.getTilapainenKotimainenOsoite()) {
                sb.append("\n--TilapainenKotimainenOsoite:");
                sb.append("\n----ParasOsoiteTieto: " + tko.getParasOsoiteTieto());
                sb.append("\n----AsuminenAlkupvm: " + tko.getAsuminenAlkupvm());
                sb.append("\n----AsuminenLoppupvm: " + tko.getAsuminenLoppupvm());
                sb.append("\n----Huoneistonumero: " + tko.getHuoneistonumero());
                sb.append("\n----Jakokirjain: " + tko.getJakokirjain());
                sb.append("\n----Katunumero: " + tko.getKatunumero());
                sb.append("\n----KatuS: " + tko.getKatuS());
                sb.append("\n----KatuR: " + tko.getKatuR());
                sb.append("\n----Porraskirjain: " + tko.getPorraskirjain());
                sb.append("\n----Postinumero: " + tko.getPostinumero());
                sb.append("\n----PostitoimipaikkaS: " + tko.getPostitoimipaikkaS());
                sb.append("\n----PostitoimipaikkaR: " + tko.getPostitoimipaikkaR());
            }
        }
        if (vtjHenkilo.getTilapainenUlkomainenOsoite() != null) {
            sb.append("\nTilapaisetUlkomaisetOsoitteet[]:");
            for (TilapainenUlkomainenOsoite tuo : vtjHenkilo.getTilapainenUlkomainenOsoite()) {
                sb.append("\n--TilapainenUlkomainenOsoite:");
                sb.append("\n----ParasOsoiteTieto: " + tuo.getParasOsoiteTieto());
                sb.append("\n----AsuminenAlkupvm: " + tuo.getAsuminenAlkupvm());
                sb.append("\n----AsuminenLoppupvm" + tuo.getAsuminenLoppupvm());
                sb.append("\n----UlkomainenLahiosoite: " + tuo.getUlkomainenLahiosoite());
                sb.append("\n----UlkomainenPaikkakunta: " + tuo.getUlkomainenPaikkakunta());
                sb.append("\n----Valtiokoodi3: " + tuo.getValtiokoodi3());
                sb.append("\n----ValtioS: " + tuo.getValtioS());
                sb.append("\n----ValtioR: " + tuo.getValtioR());
                sb.append("\n----ValtioSelvakielinen: " + tuo.getValtioSelvakielinen());
            }
        }
        if (vtjHenkilo.getUlkomainenHenkilonumero() != null) {
            sb.append("\nUlkomaisetHenkilonumerot[]:");
            for (UlkomainenHenkilonumero uhn : vtjHenkilo.getUlkomainenHenkilonumero()) {
                sb.append("\n--UlkomainenHenkilonumero:");
                sb.append("\n----UlkomainenHenkilonumero: " + uhn.getUlkomainenHenkilonumero());
                sb.append("\n----Valtiokoodi3: " + uhn.getValtiokoodi3());
                sb.append("\n----ValtioS: " + uhn.getValtioS());
                sb.append("\n----ValtioR: " + uhn.getValtioR());
                sb.append("\n----Voimassaolokoodi: " + uhn.getVoimassaolokoodi());
            }
        }
        if (vtjHenkilo.getUlkomainenPostiosoite() != null) {
            sb.append("\nUlkomaisetPostiosoitteet[]:");
            for (UlkomainenPostiosoite upo : vtjHenkilo.getUlkomainenPostiosoite()) {
                sb.append("\n--UlkomainenPostiosoite:");
                sb.append("\n----ParasOsoiteTieto: " + upo.getParasOsoiteTieto());
                sb.append("\n----PostiosoiteAlkupvm: " + upo.getPostiosoiteAlkupvm());
                sb.append("\n----PostiosoiteLoppupvm: " + upo.getPostiosoiteLoppupvm());
                sb.append("\n----UlkomainenLahiosoite: " + upo.getUlkomainenLahiosoite());
                sb.append("\n----UlkomainenPaikkakunta: " + upo.getUlkomainenPaikkakunta());
                sb.append("\n----Valtiokoodi3: " + upo.getValtiokoodi3());
                sb.append("\n----ValtioS: " + upo.getValtioS());
                sb.append("\n----ValtioR: " + upo.getValtioR());
                sb.append("\n----ValtioSelvakielinen: " + upo.getValtioSelvakielinen());
            }
        }
        if (vtjHenkilo.getUlkomainenSyntymapaikka() != null) {
            sb.append("\nUlkomainenSyntymapaikka:");
            sb.append("\n--Syntymapaikka: " + vtjHenkilo.getUlkomainenSyntymapaikka().getSyntymapaikka());
            sb.append("\n--Valtiokoodi3: " + vtjHenkilo.getUlkomainenSyntymapaikka().getValtiokoodi3());
            sb.append("\n--ValtioS: " + vtjHenkilo.getUlkomainenSyntymapaikka().getValtioS());
            sb.append("\n--ValtioR: " + vtjHenkilo.getUlkomainenSyntymapaikka().getValtioR());
            sb.append("\n--ValtioSelvakielinen: " + vtjHenkilo.getUlkomainenSyntymapaikka().getValtioSelvakielinen());
        }
        if (vtjHenkilo.getVakinainenKotimainenOsoite() != null) {
            sb.append("\nVakinainenKotimainenOsoite:");
            sb.append("\n--ParasOsoiteTieto: " + vtjHenkilo.getVakinainenKotimainenOsoite().getParasOsoiteTieto());
            sb.append("\n--AsuminenAlkupvm: " + vtjHenkilo.getVakinainenKotimainenOsoite().getAsuminenAlkupvm());
            sb.append("\n--AsuminenLoppupvm: " + vtjHenkilo.getVakinainenKotimainenOsoite().getAsuminenLoppupvm());
            sb.append("\n--Huoneistonumero: " + vtjHenkilo.getVakinainenKotimainenOsoite().getHuoneistonumero());
            sb.append("\n--Jakokirjain: " + vtjHenkilo.getVakinainenKotimainenOsoite().getJakokirjain());
            sb.append("\n--Katunumero: " + vtjHenkilo.getVakinainenKotimainenOsoite().getKatunumero());
            sb.append("\n--KatuS: " + vtjHenkilo.getVakinainenKotimainenOsoite().getKatuS());
            sb.append("\n--KatuR: " + vtjHenkilo.getVakinainenKotimainenOsoite().getKatuR());
            sb.append("\n--Porraskirjain: " + vtjHenkilo.getVakinainenKotimainenOsoite().getPorraskirjain());
            sb.append("\n--Postinumero: " + vtjHenkilo.getVakinainenKotimainenOsoite().getPostinumero());
            sb.append("\n--PostitoimipaikkaS: " + vtjHenkilo.getVakinainenKotimainenOsoite().getPostitoimipaikkaS());
            sb.append("\n--PostitoimipaikkaR: " + vtjHenkilo.getVakinainenKotimainenOsoite().getPostitoimipaikkaR());
        }
        if (vtjHenkilo.getVakinainenUlkomainenOsoite() != null) {
            sb.append("\nVakinainenUlkomainenOsoite:");
            sb.append("\n--ParasOsoiteTieto: " + vtjHenkilo.getVakinainenUlkomainenOsoite().getParasOsoiteTieto());
            sb.append("\n--AsuminenAlkupvm: " + vtjHenkilo.getVakinainenUlkomainenOsoite().getAsuminenAlkupvm());
            sb.append("\n--AsuminenLoppupvm: " + vtjHenkilo.getVakinainenUlkomainenOsoite().getAsuminenLoppupvm());
            sb.append("\n--UlkomainenLahiosoite: " + vtjHenkilo.getVakinainenUlkomainenOsoite().getUlkomainenLahiosoite());
            sb.append("\n--UlkomainenPaikkakunta: " + vtjHenkilo.getVakinainenUlkomainenOsoite().getUlkomainenPaikkakunta());
            sb.append("\n--Valtiokoodi3: " + vtjHenkilo.getVakinainenUlkomainenOsoite().getValtiokoodi3());
            sb.append("\n--ValtioS: " + vtjHenkilo.getVakinainenUlkomainenOsoite().getValtioS());
            sb.append("\n--ValtioR: " + vtjHenkilo.getVakinainenUlkomainenOsoite().getValtioR());
            sb.append("\n--ValtioSelvakielinen: " + vtjHenkilo.getVakinainenUlkomainenOsoite().getValtioSelvakielinen());
        }

        logger.error(sb.toString());
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

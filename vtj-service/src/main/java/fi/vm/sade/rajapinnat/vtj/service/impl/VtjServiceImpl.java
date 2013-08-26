package fi.vm.sade.rajapinnat.vtj.service.impl;

import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.service.VtjService;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma;
import org.springframework.cache.annotation.Cacheable;
import org.tempuri.SoSoSoap;
import org.tempuri.TeeHenkilonTunnusKyselyResponse;

/**
 * User: tommiha
 * Date: 6/26/13
 * Time: 2:23 PM
 */
public class VtjServiceImpl implements VtjService {

    private SoSoSoap soSoSoap;
    private String kayttajatunnus;
    private String salasana;

    @Cacheable(value = "vtj", key = "#hetu")
    public YksiloityHenkilo teeHenkiloKysely(String loppukayttaja, String hetu) {
        VTJHenkiloVastaussanoma vastaus = getVtjHenkiloVastaussanoma(loppukayttaja, hetu);
        return convert(vastaus);
    }

    private VTJHenkiloVastaussanoma getVtjHenkiloVastaussanoma(String loppukayttaja, String hetu) {
        TeeHenkilonTunnusKyselyResponse.TeeHenkilonTunnusKyselyResult tunnusKyselyResult = soSoSoap.teeHenkilonTunnusKysely("OPHREK", kayttajatunnus, salasana, loppukayttaja, null, hetu, null, null, null, null, null, null, null);
        VTJHenkiloVastaussanoma vastaus = (VTJHenkiloVastaussanoma) tunnusKyselyResult.getContent().get(0);
        if("0001".equals(vastaus.getPaluukoodi().getKoodi())) {
            throw new NotFoundException("Could not find person.");
        }
        return vastaus;
    }

    private YksiloityHenkilo convert(VTJHenkiloVastaussanoma vastaus) {
        YksiloityHenkilo henkilo = new YksiloityHenkilo();
        henkilo.setEtunimi(vastaus.getHenkilo().getNykyisetEtunimet().getEtunimet());
        henkilo.setSukunimi(vastaus.getHenkilo().getNykyinenSukunimi().getSukunimi());
        String kutsumanimi = vastaus.getHenkilo().getNykyinenKutsumanimi().getKutsumanimi();
        henkilo.setKutsumanimi(kutsumanimi.equals("") ? henkilo.getEtunimi() : kutsumanimi);

        String turvakieltoTieto = vastaus.getHenkilo().getTurvakielto().getTurvakieltoTieto();
        henkilo.setTurvakielto(turvakieltoTieto.equals("1") ? true : false);
        henkilo.setHetu(vastaus.getHenkilo().getHenkilotunnus().getValue());

        String sukupuolikoodi = vastaus.getHenkilo().getSukupuoli().getSukupuolikoodi();
        henkilo.setSukupuoli(sukupuolikoodi.equals("2") ? YksiloityHenkilo.Sukupuoli.NAINEN : YksiloityHenkilo.Sukupuoli.MIES);
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

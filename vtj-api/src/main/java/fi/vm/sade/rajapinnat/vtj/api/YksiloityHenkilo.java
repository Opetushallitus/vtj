package fi.vm.sade.rajapinnat.vtj.api;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * User: tommiha
 * Date: 8/26/13
 * Time: 1:46 PM
 */
public class YksiloityHenkilo implements Serializable {

    private static final long serialVersionUID = 8789597334053850029L;

    public static enum Sukupuoli {
        MIES, NAINEN
    }

    private String etunimi;

    private String kutsumanimi;

    private String sukunimi;

    private String hetu;

    private Sukupuoli sukupuoli;

    private boolean turvakielto;

    private String sahkoposti;

    private String aidinkieliKoodi;

    private List<String> kansalaisuusKoodit;

    private List<OsoiteTieto> osoitteet;

    public String getHetu() {
        return hetu;
    }

    public void setHetu(String hetu) {
        this.hetu = hetu;
    }

    public Sukupuoli getSukupuoli() {
        return sukupuoli;
    }

    public void setSukupuoli(Sukupuoli sukupuoli) {
        this.sukupuoli = sukupuoli;
    }

    public boolean isTurvakielto() {
        return turvakielto;
    }

    public void setTurvakielto(boolean turvakielto) {
        this.turvakielto = turvakielto;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public void setSukunimi(String sukunimi) {
        this.sukunimi = sukunimi;
    }

    public String getKutsumanimi() {
        return kutsumanimi;
    }

    public void setKutsumanimi(String kutsumanimi) {
        this.kutsumanimi = kutsumanimi;
    }

    public String getAidinkieliKoodi() {
        return aidinkieliKoodi;
    }

    public void setAidinkieliKoodi(String aidinkieliKoodi) {
        this.aidinkieliKoodi = aidinkieliKoodi;
    }

    public void addKansalaisuusKoodi(String kansalaisuusKoodi) {
        if (this.kansalaisuusKoodit == null) {
            this.kansalaisuusKoodit = new ArrayList<String>();
        }
        this.kansalaisuusKoodit.add(kansalaisuusKoodi);
    }

    public List<String> getKansalaisuusKoodit() {
        return kansalaisuusKoodit;
    }

    public List<OsoiteTieto> getOsoitteet() {
        return osoitteet;
    }

    public void addOsoiteTieto(OsoiteTieto osoite) {
        if (this.osoitteet == null) {
            this.osoitteet = new ArrayList<YksiloityHenkilo.OsoiteTieto>();
        }
        this.osoitteet.add(osoite);
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }




    public class OsoiteTieto {

        private String tyyppi;

        private String katuosoite;

        private String kaupunki;

        private String postinumero;

        private String maa;

        public OsoiteTieto(String tyyppi, String katuosoite,
                String postinumero, String kaupunki, String maa) {
            this.tyyppi = tyyppi;
            this.katuosoite = katuosoite;
            this.postinumero = postinumero;
            this.kaupunki = kaupunki;
            this.maa = maa;
        }

        public String getTyyppi() {
            return tyyppi;
        }

        public String getKatuosoite() {
            return katuosoite;
        }

        public String getKaupunki() {
            return kaupunki;
        }

        public String getPostinumero() {
            return postinumero;
        }

        public String getMaa() {
            return maa;
        }
    }
}

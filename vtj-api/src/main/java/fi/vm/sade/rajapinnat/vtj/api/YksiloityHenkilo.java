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

    public static enum Sukupuoli {
        MIES, NAINEN
    }

    private String etunimi;

    private String sukunimi;

    private String kutsumanimi;

    private String hetu;

    private Sukupuoli sukupuoli;

    private boolean turvakielto;

    private String sahkoposti;

    private Aidinkieli aidinkieli;

    private List<Kansalaisuus> kansalaisuus;

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

    public Aidinkieli getAidinkieli() {
        return aidinkieli;
    }

    public void setAidinkieli(Aidinkieli aidinkieli) {
        this.aidinkieli = aidinkieli;
    }

    public void addKansalaisuus(YksiloityHenkilo.Kansalaisuus kansalaisuus) {
        if (this.kansalaisuus == null) {
            this.kansalaisuus = new ArrayList<YksiloityHenkilo.Kansalaisuus>();
        }
        this.kansalaisuus.add(kansalaisuus);
    }

    public List<Kansalaisuus> getKansalaisuus() {
        return kansalaisuus;
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





    public class Aidinkieli {

        private String kielikoodi;

        private String kieli;

        public Aidinkieli(String kielikoodi, String kieli) {
            this.kielikoodi = kielikoodi;
            this.kieli = kieli;
        }

        public String getKielikoodi() {
            return kielikoodi;
        }

        public String getKieli() {
            return kieli;
        }
    }
    
    public class Kansalaisuus {

        private String kansalaisuus;

        public Kansalaisuus(String kansalaisuus) {
            this.kansalaisuus = kansalaisuus;
        }

        public String getKansalaisuus() {
            return kansalaisuus;
        }
    }
    
    public class OsoiteTieto {

        private String tyyppi;

        private String katuosoite;

        private String kaupunki;

        private String postinumero;

        private String maa;

        public OsoiteTieto(String tyyppi, String katuosoite, String postinumero, String kaupunki, String maa) {
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

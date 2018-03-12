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

    private String etunimi;

    private String kutsumanimi;

    private String sukunimi;

    private String hetu;

    private String sukupuoli;

    private boolean turvakielto;

    private String sahkoposti;

    private String aidinkieliKoodi;

    private List<String> kansalaisuusKoodit;

    private List<OsoiteTieto> osoitteet;

    private String kotikunta;

    public String getHetu() {
        return hetu;
    }

    public void setHetu(String hetu) {
        this.hetu = hetu;
    }

    public String getSukupuoli() {
        return sukupuoli;
    }

    public void setSukupuoli(String sukupuoli) {
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

    public String getKotikunta() {
        return kotikunta;
    }

    public void setKotikunta(String kotikunta) {
        this.kotikunta = kotikunta;
    }

    public String getSahkoposti() {
        return sahkoposti;
    }

    public void setSahkoposti(String sahkoposti) {
        this.sahkoposti = sahkoposti;
    }




    public class OsoiteTieto {

        private String tyyppi;

        private String katuosoiteS;

        private String katuosoiteR;

        private String kaupunkiS;

        private String kaupunkiR;

        private String postinumero;

        private String maaS;

        private String maaR;

        public OsoiteTieto(String tyyppi, String katuosoiteS,
                String katuosoiteR, String postinumero, String kaupunkiS,
                String kaupunkiR, String maaS, String maaR) {
            this.tyyppi = tyyppi;
            this.katuosoiteS = katuosoiteS;
            this.katuosoiteR = katuosoiteR;
            this.postinumero = postinumero;
            this.kaupunkiS = kaupunkiS;
            this.kaupunkiR = kaupunkiR;
            this.maaS = maaS;
            this.maaR = maaR;
        }

        public String getTyyppi() {
            return tyyppi;
        }

        public String getKatuosoiteS() {
            return katuosoiteS;
        }

        public String getKaupunkiS() {
            return kaupunkiS;
        }

        public String getMaaS() {
            return maaS;
        }

        public String getKatuosoiteR() {
            return katuosoiteR;
        }

        public String getKaupunkiR() {
            return kaupunkiR;
        }

        public String getMaaR() {
            return maaR;
        }

        public String getPostinumero() {
            return postinumero;
        }
    }
}

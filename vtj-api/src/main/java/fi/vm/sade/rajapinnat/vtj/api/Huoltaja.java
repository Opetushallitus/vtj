package fi.vm.sade.rajapinnat.vtj.api;

/**
 * Henkilön huoltaja
 */
public class Huoltaja {
    private String etunimi;

    private String sukunimi;

    private String kutsumanimi;

    private String hetu;

    public Huoltaja() {

    }

    public Huoltaja(String etunimi, String sukunimi, String kutsumanimi, String hetu) {
        this.etunimi = etunimi;
        this.sukunimi = sukunimi;
        this.kutsumanimi = kutsumanimi;
        this.hetu = hetu;
    }

    public String getEtunimi() {
        return etunimi;
    }

    public void setEtunimi(String etunimi) {
        this.etunimi = etunimi;
    }

    public String getHetu() {
        return hetu;
    }

    public void setHetu(String hetu) {
        this.hetu = hetu;
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
}

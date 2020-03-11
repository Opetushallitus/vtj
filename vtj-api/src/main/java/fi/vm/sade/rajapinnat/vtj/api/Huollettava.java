package fi.vm.sade.rajapinnat.vtj.api;

public class Huollettava {
    private String etunimet;
    private String sukunimi;
    private String hetu;

    public Huollettava() {

    }

    public Huollettava(String etunimet, String sukunimi, String hetu) {
        this.etunimet = etunimet;
        this.sukunimi = sukunimi;
        this.hetu = hetu;
    }

    public String getEtunimet() {
       return etunimet;
    }

    public String getSukunimi() {
        return sukunimi;
    }

    public String getHetu() {
        return hetu;
    }
}

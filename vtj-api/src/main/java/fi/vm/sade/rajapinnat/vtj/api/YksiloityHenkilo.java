package fi.vm.sade.rajapinnat.vtj.api;

import java.io.Serializable;

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
}

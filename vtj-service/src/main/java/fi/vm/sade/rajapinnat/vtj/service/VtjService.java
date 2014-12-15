package fi.vm.sade.rajapinnat.vtj.service;

import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma;

public interface VtjService {

    /**
     * Tekee henkilökyselyn VTJ:stä.
     *
     * @param loppukayttaja
     * @param hetu
     * @return
     * @throws fi.vm.sade.rajapinnat.vtj.NotFoundException jos henkilöä ei löydy.
     */
    YksiloityHenkilo teeHenkiloKysely(String loppukayttaja, String hetu, boolean logMessage);
}

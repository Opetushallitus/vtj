package fi.vm.sade.rajapinnat.vtj.service;

import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma;

/**
 * User: tommiha
 * Date: 6/26/13
 * Time: 2:22 PM
 */
public interface VtjService {

    /**
     * Tekee henkilökyselyn VTJ:stä.
     * @param loppukayttaja
     * @param hetu
     * @return
     * @throws fi.vm.sade.rajapinnat.vtj.NotFoundException jos henkilöä ei löydy.
     */
    VTJHenkiloVastaussanoma teeHenkiloKysely(String loppukayttaja, String hetu);
}

package fi.vm.sade.rajapinnat.vtj.service;

import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;

public interface VtjTestData {

    /**
     * Palauttaa VTJ kyselyä simuloivaa staattista
     * dataa, joka on kiinteästi kirjoitettu toteutukseen.
     */
    YksiloityHenkilo teeHakuTestidatasta(String hetu); 
}

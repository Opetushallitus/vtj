package fi.vm.sade.rajapinnat.vtj.service.impl;

import org.junit.Test;

import static fi.vm.sade.rajapinnat.vtj.service.impl.KielikoodiKludge.HEPREA_VIRHEELLINEN;
import static fi.vm.sade.rajapinnat.vtj.service.impl.KielikoodiKludge.HEPREA_KORJATTU;
import static fi.vm.sade.rajapinnat.vtj.service.impl.KielikoodiKludge.korjaaVirheellinenKoodi;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class KielikoodiKludgeTest {

    @Test
    public void korjaaVirheellisenKielikoodin() {
        assertEquals(HEPREA_KORJATTU, korjaaVirheellinenKoodi(HEPREA_VIRHEELLINEN));
    }

    @Test
    public void palauttaaAlkuperaisenKielikoodin() {
        String kielikoodi = "sv";
        assertEquals(kielikoodi, korjaaVirheellinenKoodi(kielikoodi));
    }

    @Test
    public void palauttaaNullin() {
        assertNull(korjaaVirheellinenKoodi(null));
    }
}

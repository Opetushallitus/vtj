package fi.vm.sade.rajapinnat.vtj.service.impl;

import org.junit.Test;

import static fi.vm.sade.rajapinnat.vtj.service.impl.KansallisuusKludge.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class KansallisuusKludgeTest {

    @Test
    public void korjaaVirheellisenKansallisuuskoodin() {
        assertEquals(SUDAN, korjaaVirheellinenKoodi(VANHA_SUDAN));
        assertEquals(ETIOPIA, korjaaVirheellinenKoodi(VANHA_ETIOPIA));
    }

    @Test
    public void palauttaaAlkuperaisenKansallisuuskoodin() {
        String kielikoodi = "1";
        assertEquals(kielikoodi, korjaaVirheellinenKoodi(kielikoodi));
    }

    @Test
    public void palauttaaNullin() {
        assertNull(korjaaVirheellinenKoodi(null));
    }
}

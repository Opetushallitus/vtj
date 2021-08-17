package fi.vm.sade.rajapinnat.vtj.service.impl;

import java.util.Map;

public class KansallisuusKludge {

    static final String VANHA_SUDAN = "736";
    static final String SUDAN = "729";
    static final String VANHA_ETIOPIA = "230";
    static final String ETIOPIA = "231";

    private static final Map<String, String> VIRHEELLINEN_KORJATTU = Map.of(
            VANHA_SUDAN, SUDAN,
            VANHA_ETIOPIA, ETIOPIA
    );

    /**
     * Vaihtaa (mahdollisen) virheellisen koodin oikeaan.
     *
     * @param kansallisuuskoodi kansallisuuskoodi.
     * @return oikea kansallisuuskoodi; joko annettu tai korjattu, mikäli annettu oli virheellinen.
     */
    public static String korjaaVirheellinenKoodi(String kansallisuuskoodi) {
        return kansallisuuskoodi == null ? null : VIRHEELLINEN_KORJATTU.getOrDefault(kansallisuuskoodi, kansallisuuskoodi);
    }
}

package fi.vm.sade.rajapinnat.vtj.service.impl;

import java.util.Map;
import java.util.TreeMap;

public class KielikoodiKludge {

    static final String HEPREA_VIRHEELLINEN = "iw"; // tämähän on ihan hepreaa!
    static final String HEPREA_KORJATTU = "he";

    private static final Map<String,String> VIRHEELLINEN_KORJATTU = new TreeMap<>();
    static {
        VIRHEELLINEN_KORJATTU.put(HEPREA_VIRHEELLINEN, HEPREA_KORJATTU);
    }

    /**
     * Vaihtaa (mahdollisen) virheellisen koodin oikeaan.
     *
     * @param kielikoodi kielikoodi.
     *
     * @return oikea kielikoodi; joko annettu tai korjattu, mikäli annettu oli virheellinen.
     */
    public static String korjaaVirheellinenKoodi(String kielikoodi) {
        if (kielikoodi == null) {
            return null;
        }
        String korjattu = VIRHEELLINEN_KORJATTU.get(kielikoodi.toLowerCase());
        return korjattu != null ? korjattu : kielikoodi;
    }

}

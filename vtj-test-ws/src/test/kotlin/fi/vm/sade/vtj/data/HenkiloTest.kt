package fi.vm.sade.vtj.data

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HenkiloTest {

    private val helper = HenkiloHelper()

    @Test
    fun `sukupuoli tunnistetaan hetusta mieheksi`() {
        val hetu = "111111-1111"
        assertEquals(Sukupuoli.MIES, helper.sukupuoli(hetu))
    }

    @Test
    fun `sukupuoli tunnistetaan hetusta naiseksi`() {
        val hetu = "111111-1121"
        assertEquals(Sukupuoli.NAINEN, helper.sukupuoli(hetu))
    }
}

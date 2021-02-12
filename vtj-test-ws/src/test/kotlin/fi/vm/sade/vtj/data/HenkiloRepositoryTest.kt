package fi.vm.sade.vtj.data

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest

@DataJpaTest
class HenkiloRepositoryTest {

    @Autowired lateinit var henkiloRepository: HenkiloRepository

    @Test
    fun `findByHenkilotunnus palauttaa henkilon hetulla`() {
        val henkilotunnus = "010156-9994"
        val henkilo = henkiloRepository.findByHenkilotunnus(henkilotunnus)
        Assertions.assertNotNull(henkilo)
    }

    @Test
    fun `findByHenkilotunnus palauttaa nullin, kun henkiloa ei loydy`() {
        val henkilotunnus = "313131-3100" // ei todellakaan oo
        val henkilo = henkiloRepository.findByHenkilotunnus(henkilotunnus)
        Assertions.assertNull(henkilo)
    }

    @Test
    fun `findByHenkilotunnus palauttaa huollettavat`() {
        val henkilotunnus = "210281-9988"
        val henkilo = henkiloRepository.findByHenkilotunnus(henkilotunnus)
        Assertions.assertEquals(2, henkilo?.huollettavat!!.size)
    }

    @Test
    fun `findByHenkilotunnus palauttaa huoltajan`() {
        val henkilotunnus = "190306A9850"
        val henkilo = henkiloRepository.findByHenkilotunnus(henkilotunnus)
        Assertions.assertFalse(henkilo?.huoltajat!!.isEmpty())
    }
}

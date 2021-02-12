package fi.vm.sade.vtj.service

import fi.vm.sade.vtj.client.OppijanumerorekisteriClient
import fi.vm.sade.vtj.data.HenkiloHelper
import fi.vm.sade.vtj.data.HenkiloRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito

class HenkiloServiceTest {

    private val henkiloRepository = Mockito.mock(HenkiloRepository::class.java)
    private val onrClient = Mockito.mock(OppijanumerorekisteriClient::class.java)
    private val hetu = "999999-9999"
    private val henkilo = HenkiloHelper().dummyHenkilo()

    @Test
    fun `palauttaa null, kun onrEnabled false, eika henkiloa loydy`() {
        Mockito.`when`(henkiloRepository.findByHenkilotunnus(hetu)).thenReturn(null)
        val service = HenkiloService(henkiloRepository, onrClient, false)
        Assertions.assertNull(service.findByHenkilotunnus(hetu))
    }

    @Test
    fun `palauttaa henkilon, kun onrEnabled false ja henkilo loytyy`() {
        Mockito.`when`(henkiloRepository.findByHenkilotunnus(hetu)).thenReturn(henkilo)
        val service = HenkiloService(henkiloRepository, onrClient, false)
        Assertions.assertEquals(henkilo, service.findByHenkilotunnus(hetu))
    }

    @Test
    fun `palauttaa null, kun onrEnabled true, eika henkiloa loydy`() {
        Mockito.`when`(henkiloRepository.findByHenkilotunnus(hetu)).thenReturn(null)
        Mockito.`when`(onrClient.getByHetu(hetu)).thenReturn(null)
        val service = HenkiloService(henkiloRepository, onrClient, true)
        Assertions.assertNull(service.findByHenkilotunnus(hetu))
    }

    @Test
    fun `palauttaa henkilon, kun onrEnabled true ja henkilo loytyy`() {
        Mockito.`when`(henkiloRepository.findByHenkilotunnus(hetu)).thenReturn(null)
        Mockito.`when`(onrClient.getByHetu(hetu)).thenReturn(henkilo)
        val service = HenkiloService(henkiloRepository, onrClient, true)
        Assertions.assertEquals(henkilo, service.findByHenkilotunnus(hetu))
    }
}

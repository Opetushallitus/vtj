package fi.vm.sade.vtj.service

import fi.vm.sade.vtj.client.OppijanumerorekisteriClient
import fi.vm.sade.vtj.data.Henkilo
import fi.vm.sade.vtj.data.HenkiloRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class HenkiloService(
        @Autowired val henkiloRepository: HenkiloRepository,
        @Autowired val onrClient: OppijanumerorekisteriClient,
        @Value("\${vtj-test-ws.datastore.oppijanumerorekisteri.enabled}") val onrEnabled: Boolean
) {

    fun findByHenkilotunnus(henkilotunnus: String): Henkilo? {
        val henkilo = henkiloRepository.findByHenkilotunnus(henkilotunnus)
        if (!onrEnabled) return henkilo
        return henkilo ?: onrClient.getByHetu(henkilotunnus)
    }

}

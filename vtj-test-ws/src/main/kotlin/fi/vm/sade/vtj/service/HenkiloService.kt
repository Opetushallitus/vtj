package fi.vm.sade.vtj.service

import fi.vm.sade.vtj.client.OppijanumerorekisteriClient
import fi.vm.sade.vtj.data.Henkilo
import fi.vm.sade.vtj.data.HenkiloRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class HenkiloService(
        @Autowired val henkiloRepository: HenkiloRepository,
        @Autowired val onrClient: OppijanumerorekisteriClient,
        @Value("\${vtj-test-ws.oppijanumerorekisteri.enabled}") val onrEnabled: Boolean
) {

    private val logger = LoggerFactory.getLogger(HenkiloService::class.java)

    fun findByHenkilotunnus(henkilotunnus: String): Henkilo? {
        val repositoryHenkilo = henkiloRepository.findByHenkilotunnus(henkilotunnus)
        logger.info("Henkilö $henkilotunnus löytyi henkiloRepositorysta: ${repositoryHenkilo != null}")
        if (!onrEnabled || repositoryHenkilo != null) return repositoryHenkilo
        val onrHenkilo = onrClient.getByHetu(henkilotunnus)
        logger.info("Henkilö $henkilotunnus löytyi oppijanumerorekisteristä: ${onrHenkilo != null}")
        return onrHenkilo
    }

}

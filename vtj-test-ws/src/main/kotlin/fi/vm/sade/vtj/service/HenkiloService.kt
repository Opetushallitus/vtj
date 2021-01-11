package fi.vm.sade.vtj.service

import fi.vm.sade.vtj.data.Henkilo
import fi.vm.sade.vtj.data.HenkiloRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HenkiloService(@Autowired val henkiloRepository: HenkiloRepository) {

    fun findByHenkilotunnus(henkilotunnus: String): Henkilo? {
        return henkiloRepository.findByHenkilotunnus(henkilotunnus)
    }

}

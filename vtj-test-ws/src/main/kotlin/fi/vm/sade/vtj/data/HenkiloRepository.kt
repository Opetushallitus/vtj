package fi.vm.sade.vtj.data

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface HenkiloRepository: JpaRepository<Henkilo,Long> {

    fun findByHenkilotunnus(henkilotunnus: String): Henkilo?

}

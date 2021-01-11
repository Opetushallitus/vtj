package fi.vm.sade.vtj.config

import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.tempuri.TeeHenkilonTunnusKyselyResponse

@Configuration
class JaxbMarshallerConfig {

    @Bean
    fun jaxb2Marshaller(): Jaxb2Marshaller {
        val marshaller = Jaxb2Marshaller()
        marshaller.setClassesToBeBound(TeeHenkilonTunnusKyselyResponse::class.java, VTJHenkiloVastaussanoma::class.java)
        return marshaller
    }

}

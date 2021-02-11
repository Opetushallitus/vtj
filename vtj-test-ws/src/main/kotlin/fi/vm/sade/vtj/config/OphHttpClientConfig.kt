package fi.vm.sade.vtj.config

import fi.vm.sade.javautils.http.auth.CasAuthenticator
import fi.vm.sade.javautils.http.OphHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OphHttpClientConfig(
        @Value("\${vtj-test-ws.oppijanumerorekisteri.host}") val onrHost: String,
        @Value("\${vtj-test-ws.oppijanumerorekisteri.username}") val onrUsername: String,
        @Value("\${vtj-test-ws.oppijanumerorekisteri.password}") val onrPassword: String
) {

    @Bean
    fun onrHttpClient(): OphHttpClient {
        val authenticator = CasAuthenticator.Builder()
                .username(onrUsername)
                .password(onrPassword)
                .webCasUrl(onrHost + CAS_PATH)
                .casServiceUrl(onrHost + CAS_SERVICE_PATH)
                .build()
        return OphHttpClient.Builder(CALLER_ID).authenticator(authenticator).build()
    }

}

const val CALLER_ID = "1.2.246.562.10.00000000001.vtj-test-ws"
const val CAS_PATH = "/cas"
const val CAS_SERVICE_PATH = "/oppijanumerorekisteri-service/j_spring_cas_security_check"

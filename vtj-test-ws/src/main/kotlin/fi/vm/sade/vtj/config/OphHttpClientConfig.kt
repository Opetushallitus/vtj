package fi.vm.sade.vtj.config

import fi.vm.sade.javautils.httpclient.OphHttpClient
import fi.vm.sade.javautils.httpclient.apache.ApacheOphHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OphHttpClientConfig {

    @Bean
    fun onrHttpClient(): OphHttpClient {
        return ApacheOphHttpClient.createDefaultOphClient(CALLER_ID, null)
    }

}

const val CALLER_ID = "1.2.246.562.10.00000000001.vtj-test-ws"

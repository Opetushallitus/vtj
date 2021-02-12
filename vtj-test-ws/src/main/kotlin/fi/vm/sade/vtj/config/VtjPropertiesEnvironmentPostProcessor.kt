package fi.vm.sade.vtj.config

import org.springframework.boot.SpringApplication
import org.springframework.boot.env.EnvironmentPostProcessor
import org.springframework.boot.logging.DeferredLog
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.ResourcePropertySource
import java.nio.file.Paths

class VtjPropertiesEnvironmentPostProcessor: EnvironmentPostProcessor {

    private val logger = DeferredLog() // normaali lokitus ei vielä käytettävissä
    private val defaultVtjPropertiesPath = System.getProperty("user.home") + "/oph-configuration/vtj-service.properties"

    override fun postProcessEnvironment(environment: ConfigurableEnvironment?, application: SpringApplication?) {
        application!!.addInitializers(ApplicationContextInitializer<ConfigurableApplicationContext> {
            logger.replayTo(VtjPropertiesEnvironmentPostProcessor::class.java)
        }) // sovelluksen startattua ohjataan lokitus normaalille loggerille
        val vtjPropertiesPath = System.getProperty(VTJ_PROPERTIES_FILE_KEY, defaultVtjPropertiesPath)
        val vtjPropertiesResource = FileSystemResource(Paths.get(vtjPropertiesPath))
        if (vtjPropertiesResource.exists()) {
            logger.info("VTJ-palvelukonfiguraatio löytyi: ${vtjPropertiesResource.path}")
            val vtjPropertySource = ResourcePropertySource(vtjPropertiesResource)
            environment!!.propertySources.addFirst(vtjPropertySource)
        } else {
            logger.warn("VTJ-palvelukonfiguraatiota ei löydy: ${vtjPropertiesResource.path}")
        }
    }
}

const val VTJ_PROPERTIES_FILE_KEY = "vtj-service.properties.file"

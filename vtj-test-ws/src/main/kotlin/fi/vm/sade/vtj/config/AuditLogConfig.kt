package fi.vm.sade.vtj.config

import fi.vm.sade.auditlog.ApplicationType
import fi.vm.sade.auditlog.Audit
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AuditLogConfig {

    private val logger = LoggerFactory.getLogger(LOGGER_NAME)

    @Bean
    fun audit(): Audit {
        return Audit(logger::info, "vtj-test-ws", ApplicationType.BACKEND)
    }
}

const val LOGGER_NAME = "fi.vm.sade.vtj.testws.AuditLogger"

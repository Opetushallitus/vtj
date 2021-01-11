package fi.vm.sade.vtj

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.ws.config.annotation.EnableWs

@SpringBootApplication
@EnableWs
class VtjWebServiceApplication

fun main(args: Array<String>) {
	runApplication<VtjWebServiceApplication>(*args)
}

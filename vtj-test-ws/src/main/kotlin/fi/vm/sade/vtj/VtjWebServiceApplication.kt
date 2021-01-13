package fi.vm.sade.vtj

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class VtjWebServiceApplication

fun main(args: Array<String>) {
	runApplication<VtjWebServiceApplication>(*args)
}

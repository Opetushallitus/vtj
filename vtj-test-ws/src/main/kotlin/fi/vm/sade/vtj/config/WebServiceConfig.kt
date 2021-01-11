package fi.vm.sade.vtj.config

import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.ws.config.annotation.EnableWs
import org.springframework.ws.config.annotation.WsConfigurerAdapter
import org.springframework.ws.transport.http.MessageDispatcherServlet
import org.springframework.ws.wsdl.wsdl11.SimpleWsdl11Definition
import org.springframework.ws.wsdl.wsdl11.Wsdl11Definition

@EnableWs
@Configuration
class WebServiceConfig: WsConfigurerAdapter() {

    @Bean
    fun messageDispatcherServlet(applicationContext: ApplicationContext): ServletRegistrationBean<MessageDispatcherServlet> {
        val servlet = MessageDispatcherServlet()
        servlet.setApplicationContext(applicationContext)
        servlet.isTransformWsdlLocations = true
        return ServletRegistrationBean(servlet, "/*")
    }

    @Bean(name = ["vtj"])
    fun wsdl11Definition(): Wsdl11Definition {
        return SimpleWsdl11Definition(ClassPathResource("wsdl/SoSo.wsdl"))
    }

}

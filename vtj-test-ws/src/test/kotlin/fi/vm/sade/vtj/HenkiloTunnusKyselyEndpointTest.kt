package fi.vm.sade.vtj

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContext
import org.springframework.ws.test.server.MockWebServiceClient
import org.springframework.ws.test.server.RequestCreators.withPayload
import org.springframework.ws.test.server.ResponseMatchers
import org.springframework.xml.transform.StringSource

@SpringBootTest
class HenkiloTunnusKyselyEndpointTest {
    companion object {
        const val PAYLOAD =
                """
                <TeeHenkilonTunnusKysely xmlns="http://tempuri.org/">
                    <Henkilotunnus>010156-9994</Henkilotunnus>
                </TeeHenkilonTunnusKysely>
                """
    }

    @Autowired lateinit var applicationContext: ApplicationContext
    lateinit var mockWebServiceClient: MockWebServiceClient

    @BeforeEach
    fun createClient() {
        mockWebServiceClient = MockWebServiceClient.createClient(applicationContext)
    }

    @Test
    fun returnsResponse() {
        val payload = StringSource(PAYLOAD)
        mockWebServiceClient.sendRequest(withPayload(payload)).andExpect(ResponseMatchers.noFault())
    }

}

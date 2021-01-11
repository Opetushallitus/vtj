package fi.vm.sade.vtj

import fi.vm.sade.vtj.data.Henkilo
import fi.vm.sade.vtj.data.HenkiloHelper
import fi.vm.sade.vtj.service.HenkiloService
import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import org.springframework.ws.server.endpoint.annotation.Endpoint
import org.springframework.ws.server.endpoint.annotation.PayloadRoot
import org.springframework.ws.server.endpoint.annotation.RequestPayload
import org.springframework.ws.server.endpoint.annotation.ResponsePayload
import org.tempuri.TeeHenkilonTunnusKysely
import org.tempuri.TeeHenkilonTunnusKyselyResponse
import java.io.StringReader
import java.io.StringWriter
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

@Endpoint
class HenkiloTunnusKyselyEndpoint(
        @Autowired val marshaller: Jaxb2Marshaller,
        @Autowired val henkiloService: HenkiloService) {

    private val helper = HenkiloHelper()

    @PayloadRoot(namespace = NAMESPACE, localPart = "TeeHenkilonTunnusKysely")
    @ResponsePayload
    fun teeHenkiloTunnusKysely(@RequestPayload kysely: TeeHenkilonTunnusKysely): StreamSource {
        val tulos = TeeHenkilonTunnusKyselyResponse.TeeHenkilonTunnusKyselyResult()
        tulos.content.add(haeHenkilo(kysely.henkilotunnus!!))
        val vastaus = TeeHenkilonTunnusKyselyResponse()
        vastaus.teeHenkilonTunnusKyselyResult = tulos
        val writer = StringWriter()
        marshaller.marshal(vastaus, StreamResult(writer))
        return StreamSource(StringReader(writer.toString()))
    }

    private fun haeHenkilo(hetu: String): VTJHenkiloVastaussanoma {
        val vastaussanoma = VTJHenkiloVastaussanoma()
        vastaussanoma.paluukoodi = VTJHenkiloVastaussanoma.Paluukoodi()
        val henkilo = henkiloService.findByHenkilotunnus(hetu)
        if (henkilo != null) {
            vastaussanoma.henkilo = helper.muunnaHenkilo(henkilo)
            vastaussanoma.paluukoodi.koodi = HAKU_ONNISTUI
        } else {
            vastaussanoma.paluukoodi.koodi = HAKUPERUSTEELLA_EI_LOYDY
        }
        return vastaussanoma
    }

}

const val NAMESPACE = "http://tempuri.org/"
const val HAKU_ONNISTUI = "0000"
const val HAKUPERUSTEELLA_EI_LOYDY = "0001"

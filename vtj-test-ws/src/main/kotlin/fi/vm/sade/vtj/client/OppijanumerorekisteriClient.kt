package fi.vm.sade.vtj.client

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fi.vm.sade.javautils.http.OphHttpClient
import fi.vm.sade.javautils.http.OphHttpRequest
import fi.vm.sade.oppijanumerorekisteri.dto.HenkiloReadDto
import fi.vm.sade.oppijanumerorekisteri.dto.HuoltajaDto
import fi.vm.sade.oppijanumerorekisteri.dto.YhteystiedotRyhmaDto
import fi.vm.sade.oppijanumerorekisteri.dto.YhteystietoTyyppi
import fi.vm.sade.vtj.data.*
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class OppijanumerorekisteriClient(
        @Autowired val onrHttpClient: OphHttpClient,
        @Value("\${vtj-test-ws.oppijanumerorekisteri.address}") val onrAddress: String,
        @Autowired val objectMapper: ObjectMapper
) {

    private val logger = LoggerFactory.getLogger(OppijanumerorekisteriClient::class.java)
    private val katuosoitePattern = Pattern.compile("(\\w+)\\s+(\\d+)\\s*(\\p{Alpha}?)\\s*(\\d?)\\s*(\\p{Alpha}?)")
    private val henkiloHelper = HenkiloHelper()

    fun getByHetu(henkilotunnus: String): Henkilo? {
        val address = onrAddress + HETU_HAKU_PATH_PATTERN.format(henkilotunnus)
        val henkiloRequest = OphHttpRequest.Builder.get(address).build()
        val henkiloDto = onrHttpClient.execute<HenkiloReadDto>(henkiloRequest)
                .expectedStatus(200, 204, 404)
                .mapWith { objectMapper.readValue(it, HenkiloReadDto::class.java) }
                .orElse(null)
                ?: return null
        logger.info("Oppijanumerorekisteristä löytyi oppija: ${henkiloDto.oidHenkilo}")
        val huoltajatAddress = onrAddress + HUOLTAJA_HAKU_PATH_PATTERN.format(henkiloDto.oidHenkilo)
        val huoltajatRequest = OphHttpRequest.Builder.get(huoltajatAddress).build()
        val huoltajat = onrHttpClient.execute<List<HuoltajaDto>>(huoltajatRequest)
                .expectedStatus(200, 204, 404)
                .mapWith { objectMapper.readValue(it, object : TypeReference<List<HuoltajaDto>>() {}) }
                .map { huoltajaList ->
                    logger.info("Oppijalle löytyi ${huoltajaList.size} huoltajaa.")
                    huoltajaList.map {
                        val yhteystiedot = getYhteystiedot(it.yhteystiedotRyhma)
                        Henkilo(-1L, it.hetu, false, henkiloHelper.sukupuoli(it.hetu),
                                it.etunimet, it.sukunimi, it.aidinkieli?.kieliKoodi?:"fi", "",
                                null, yhteystiedot.first, null,
                                yhteystiedot.second, null, it.kotikunta, emptySet(), emptySet())
                    }.toSet()
                }.orElse(emptySet())
        return convertHenkilo(henkiloDto, huoltajat)
    }

    private fun convertHenkilo(henkilo: HenkiloReadDto, huoltajat: Set<Henkilo>): Henkilo {
        val yhteystiedot = getYhteystiedot(henkilo.yhteystiedotRyhma)
        return Henkilo(
                -1L, henkilo.hetu, henkilo.turvakielto, henkiloHelper.sukupuoli(henkilo.hetu), henkilo.etunimet,
                henkilo.sukunimi, henkilo.aidinkieli?.kieliKoodi?:"fi",
                henkilo.kansalaisuus.map { it.kansalaisuusKoodi }.first(), null, yhteystiedot.first, null,
                yhteystiedot.second, null, null, huoltajat, emptySet()
        )
    }

    private fun getYhteystiedot(yhteystietoRyhmat: Set<YhteystiedotRyhmaDto>): Pair<KotimainenOsoite?, UlkomainenOsoite?> {
        var kotimainenOsoite: KotimainenOsoite? = null
        var ulkomainenOsoite: UlkomainenOsoite? = null
        yhteystietoRyhmat.forEach { ryhma ->
            if (ryhma.ryhmaKuvaus == YHTEYSTIETOTYYPPI_KOTIMAINEN_OSOITE) {
                kotimainenOsoite = KotimainenOsoite()
                ryhma.yhteystieto.forEach {
                    when(it.yhteystietoTyyppi) {
                        YhteystietoTyyppi.YHTEYSTIETO_KATUOSOITE -> {
                            val matchResult = katuosoitePattern.matcher(it.yhteystietoArvo).toMatchResult()
                            kotimainenOsoite!!.katu_fi = matchResult.group(1)
                            kotimainenOsoite!!.katunumero = matchResult.group(2).toInt()
                            kotimainenOsoite!!.porraskirjain = matchResult.group(3).toCharArray().firstOrNull()
                            kotimainenOsoite!!.huoneistonumero = matchResult.group(4).toIntOrNull()
                            kotimainenOsoite!!.jakokirjain = matchResult.group(5).toCharArray().firstOrNull()
                        }
                        YhteystietoTyyppi.YHTEYSTIETO_POSTINUMERO -> kotimainenOsoite!!.postinumero = it.yhteystietoArvo
                        YhteystietoTyyppi.YHTEYSTIETO_KAUPUNKI -> kotimainenOsoite!!.postitoimipaikka_fi = it.yhteystietoArvo
                        else -> { /* ignore */ }
                    }
                }
            } else if (ryhma.ryhmaKuvaus == YHTEYSTIETOTYYPPI_ULKOMAINEN_OSOITE) {
                ulkomainenOsoite = UlkomainenOsoite()
                ryhma.yhteystieto.forEach {
                    when(it.yhteystietoTyyppi) {
                        YhteystietoTyyppi.YHTEYSTIETO_KATUOSOITE -> ulkomainenOsoite!!.lahiosoite = it.yhteystietoArvo
                        YhteystietoTyyppi.YHTEYSTIETO_KUNTA -> ulkomainenOsoite!!.paikkakunta = it.yhteystietoArvo
                        YhteystietoTyyppi.YHTEYSTIETO_MAA -> ulkomainenOsoite!!.valtio_fi = it.yhteystietoArvo
                        else -> { /* ignore */ }
                    }
                }
            }
        }
        return Pair(kotimainenOsoite, ulkomainenOsoite)
    }

}

const val HETU_HAKU_PATH_PATTERN = "/henkilo/hetu=%s"
const val HUOLTAJA_HAKU_PATH_PATTERN = "/henkilo/%s/huoltajat"
const val YHTEYSTIETOTYYPPI_KOTIMAINEN_OSOITE = "yhteystietotyyppi4"
const val YHTEYSTIETOTYYPPI_ULKOMAINEN_OSOITE = "yhteystietotyyppi5"

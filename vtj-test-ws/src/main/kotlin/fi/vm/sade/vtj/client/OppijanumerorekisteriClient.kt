package fi.vm.sade.vtj.client

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import fi.vm.sade.javautils.httpclient.OphHttpClient
import fi.vm.sade.oppijanumerorekisteri.dto.HenkiloReadDto
import fi.vm.sade.oppijanumerorekisteri.dto.HuoltajaDto
import fi.vm.sade.oppijanumerorekisteri.dto.YhteystiedotRyhmaDto
import fi.vm.sade.oppijanumerorekisteri.dto.YhteystietoTyyppi
import fi.vm.sade.vtj.data.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.regex.Pattern

@Component
class OppijanumerorekisteriClient(
        @Autowired val onrHttpClient: OphHttpClient,
        @Value("\${vtj-test-ws.datastore.oppijanumerorekisteri.host}") val onrHost: String
) {

    private val katuosoitePattern = Pattern.compile("(\\w+)\\s+(\\d+)\\s*(\\p{Alpha}?)\\s*(\\d?)\\s*(\\p{Alpha}?)")
    private val henkiloHelper = HenkiloHelper()
    private val objectMapper = ObjectMapper()

    fun getByHetu(henkilotunnus: String): Henkilo? {
        val address = onrHost + HETU_HAKU_PATH_PATTERN.format(henkilotunnus)
        return onrHttpClient.get(address).expectStatus(200).accept(JSON_MEDIATYPE).execute {
            val henkiloDto = objectMapper.readValue(it.asInputStream(), HenkiloReadDto::class.java)
            val huoltajatAddress = onrHost + HUOLTAJA_HAKU_PATH_PATTERN.format(henkiloDto.oidHenkilo)
            val huoltajat = onrHttpClient.get(huoltajatAddress).expectStatus(200).accept(JSON_MEDIATYPE).execute {
                objectMapper.readValue(it.asInputStream(), object : TypeReference<List<HuoltajaDto>>() {})
            }.map {
                val yhteystiedot = getYhteystiedot(it.yhteystiedotRyhma)
                Henkilo(-1L, it.hetu, false, henkiloHelper.sukupuoli(it.hetu), it.etunimet, it.sukunimi,
                        it.aidinkieli.kieliKoodi, "",null, yhteystiedot.first, null,
                        yhteystiedot.second, null, it.kotikunta, emptySet(), emptySet())
            }.toSet()
            convertHenkilo(henkiloDto, huoltajat)
        }
    }

    private fun convertHenkilo(henkilo: HenkiloReadDto, huoltajat: Set<Henkilo>): Henkilo {
        val yhteystiedot = getYhteystiedot(henkilo.yhteystiedotRyhma)
        return Henkilo(
                -1L, henkilo.hetu, henkilo.turvakielto, Sukupuoli.valueOf(henkilo.sukupuoli), henkilo.etunimet,
                henkilo.sukunimi, henkilo.aidinkieli.kieliKoodi,
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
const val JSON_MEDIATYPE = "application/json"
const val YHTEYSTIETOTYYPPI_KOTIMAINEN_OSOITE = "yhteystietotyyppi4"
const val YHTEYSTIETOTYYPPI_ULKOMAINEN_OSOITE = "yhteystietotyyppi5"

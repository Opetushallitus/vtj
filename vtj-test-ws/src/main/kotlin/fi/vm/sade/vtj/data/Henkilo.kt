package fi.vm.sade.vtj.data

import fi.vrk.xml.schema.vtjkysely.VTJHenkiloVastaussanoma
import javax.persistence.*
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

@Entity
@Table(name = "henkilo")
class Henkilo(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        @Column(unique = true) @Pattern(regexp = "^[0-9]{6}[+\\-A][0-9]{3}[A-Z0-9]$")
        var henkilotunnus: String,
        var turvakielto: Boolean = false,
        @Enumerated(EnumType.STRING)
        var sukupuoli: Sukupuoli?,
        @Size(min = 1, max = 100)
        var etunimet: String,
        @Size(min = 1, max = 100)
        var sukunimi: String,
        @Size(min = 2, max = 2)
        var aidinkieli: String,
        @Pattern(regexp = "^[0-9]{3}$")
        var kansalaisuus: String,
        @Size(min = 5, max = 100)
        var sahkoposti: String?,
        @OneToOne @JoinColumn(name = "kotimainen_osoite_id")
        var kotimainenOsoite: KotimainenOsoite?,
        @OneToOne @JoinColumn(name = "kotimainen_postiosoite_id")
        var kotimainenPostiosoite: KotimainenOsoite?,
        @OneToOne @JoinColumn(name = "ulkomainen_osoite_id")
        var ulkomainenOsoite: UlkomainenOsoite?,
        @OneToOne @JoinColumn(name = "ulkomainen_postiosoite_id")
        var ulkomainenPostiosoite: UlkomainenOsoite?,
        @Pattern(regexp = "^[0-9]{3}$")
        var kotikunta: String?,
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "henkilo_huoltaja",
                joinColumns = [JoinColumn(name = "huollettava_id")],
                inverseJoinColumns = [JoinColumn(name = "huoltaja_id")]
        )
        var huoltajat: Set<Henkilo> = emptySet(),
        @ManyToMany(fetch = FetchType.EAGER)
        @JoinTable(
                name = "henkilo_huoltaja",
                joinColumns = [JoinColumn(name = "huoltaja_id")],
                inverseJoinColumns = [JoinColumn(name = "huollettava_id")]
        )
        var huollettavat: Set<Henkilo> = emptySet())

enum class Sukupuoli(val koodi: String) {
        MIES("1"),
        NAINEN("2")
}

@Entity
@Table(name = "kotimainen_osoite")
class KotimainenOsoite(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        @Size(min = 1, max = 100) var katu_fi: String?,
        @Size(min = 1, max = 100) var katu_sv: String?,
        @Positive var katunumero: Int,
        var porraskirjain: Char?,
        @Positive var huoneistonumero: Int?,
        var jakokirjain: Char?,
        @Size(min = 5, max = 5) var postinumero: String,
        @Size(min = 1, max = 100) var postitoimipaikka_fi: String?,
        @Size(min = 1, max = 100) var postitoimipaikka_sv: String?
)

@Entity
@Table(name = "ulkomainen_osoite")
data class UlkomainenOsoite(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        var id: Long,
        @Size(min = 1, max = 100) var lahiosoite: String,
        @Size(min = 1, max = 100) var paikkakunta: String,
        @Size(min = 1, max = 100) var valtio_fi: String?,
        @Size(min = 1, max = 100) var valtio_sv: String?
)

class HenkiloHelper {

        fun muunnaHenkilo(henkilo: Henkilo): VTJHenkiloVastaussanoma.Henkilo {
                val muunnettu = henkilo()
                muunnettu.nykyinenKutsumanimi.kutsumanimi = henkilo.etunimet.substringBefore(" ")
                muunnettu.nykyisetEtunimet.etunimet = henkilo.etunimet
                muunnettu.nykyinenSukunimi.sukunimi = henkilo.sukunimi
                muunnettu.aidinkieli.kielikoodi = henkilo.aidinkieli
                muunnettu.henkilotunnus.value = henkilo.henkilotunnus
                muunnettu.sukupuoli.sukupuolikoodi = henkilo.sukupuoli?.koodi
                val kansalaisuus = VTJHenkiloVastaussanoma.Henkilo.Kansalaisuus()
                kansalaisuus.kansalaisuuskoodi3 = henkilo.kansalaisuus
                muunnettu.kansalaisuus.add(kansalaisuus)
                muunnettu.kotikunta.kuntanumero = henkilo.kotikunta
                henkilo.kotimainenOsoite?.let {
                        val osoite = VTJHenkiloVastaussanoma.Henkilo.VakinainenKotimainenOsoite()
                        osoite.katuS = it.katu_fi
                        osoite.katuR = it.katu_sv
                        osoite.katunumero = it.katunumero.toString()
                        osoite.porraskirjain = it.porraskirjain?.toString()
                        osoite.huoneistonumero = it.huoneistonumero?.toString()
                        osoite.jakokirjain = it.jakokirjain?.toString()
                        osoite.postinumero = it.postinumero
                        osoite.postitoimipaikkaS = it.postitoimipaikka_fi
                        osoite.postitoimipaikkaR = it.postitoimipaikka_sv
                        muunnettu.vakinainenKotimainenOsoite = osoite
                }
                henkilo.ulkomainenOsoite?.let {
                        val osoite = VTJHenkiloVastaussanoma.Henkilo.VakinainenUlkomainenOsoite()
                        osoite.ulkomainenLahiosoite = it.lahiosoite
                        osoite.ulkomainenPaikkakunta = it.paikkakunta
                        osoite.valtioS = it.valtio_fi
                        osoite.valtioR = it.valtio_sv
                        muunnettu.vakinainenUlkomainenOsoite = osoite
                }
                henkilo.huoltajat.forEach {
                        val huoltaja = VTJHenkiloVastaussanoma.Henkilo.Huoltaja()
                        huoltaja.henkilotunnus = it.henkilotunnus
                        huoltaja.nykyisetEtunimet = VTJHenkiloVastaussanoma.Henkilo.Huoltaja.NykyisetEtunimet()
                        huoltaja.nykyisetEtunimet.etunimet = henkilo.etunimet
                        huoltaja.nykyinenSukunimi = VTJHenkiloVastaussanoma.Henkilo.Huoltaja.NykyinenSukunimi()
                        huoltaja.nykyinenSukunimi.sukunimi = henkilo.sukunimi
                        muunnettu.huoltaja.add(huoltaja)
                }
                muunnettu.huoltajaLkm = henkilo.huoltajat.size.toString()
                henkilo.huollettavat.forEach {
                        val huollettava = VTJHenkiloVastaussanoma.Henkilo.Huollettava()
                        huollettava.henkilotunnus = it.henkilotunnus
                        huollettava.etunimet = it.etunimet
                        huollettava.sukunimi = it.sukunimi
                        muunnettu.huollettava.add(huollettava)
                }
                return muunnettu
        }

        // alustaa kenttiä ei-nulleiksi; ihanaa generoitua koodia!
        private fun henkilo(): VTJHenkiloVastaussanoma.Henkilo {
                val henkilo = VTJHenkiloVastaussanoma.Henkilo()
                henkilo.henkilotunnus = VTJHenkiloVastaussanoma.Henkilo.Henkilotunnus()
                henkilo.sukupuoli = VTJHenkiloVastaussanoma.Henkilo.Sukupuoli()
                henkilo.turvakielto = VTJHenkiloVastaussanoma.Henkilo.Turvakielto()
                henkilo.nykyinenKutsumanimi = VTJHenkiloVastaussanoma.Henkilo.NykyinenKutsumanimi()
                henkilo.nykyisetEtunimet = VTJHenkiloVastaussanoma.Henkilo.NykyisetEtunimet()
                henkilo.nykyinenSukunimi = VTJHenkiloVastaussanoma.Henkilo.NykyinenSukunimi()
                henkilo.aidinkieli = VTJHenkiloVastaussanoma.Henkilo.Aidinkieli()
                henkilo.kotikunta = VTJHenkiloVastaussanoma.Henkilo.Kotikunta()
                return henkilo
        }
}

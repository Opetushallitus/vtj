# 💀 VTJ-service on korvattu suorilla kutsuilla VTJkysely-rajapintaan esim. [Oppijanumerorekisterissä](https://github.com/Opetushallitus/oppijanumerorekisteri/tree/master/oppijanumerorekisteri-service/src/main/java/fi/vm/sade/oppijanumerorekisteri/vtjkysely) 💀

# VTJ-service

Palvelu henkilötietojen kyselemiseen väestötietojärjestelmästä. Koostuu seuraavista moduleista:
* vtj-api - rajapinnan palauttamat luokat
* vtj-remote-api - VTJ:n webservicen määrittely, siitä generoidut luokat ja client
* vtj-service - palvelurajapinta kyselyjen tekemiseen
* vtj-test-ws - webservice-toteutus testiympäristöihin

## Technologies & Frameworks

Below is non-exhaustive list of the key technologies & frameworks used in the project.

### Backend

* Spring Framework
* Spring Boot
* Spring Security (CAS)
* Postgresql
* JPA
* Flyway
* Swagger

### Build tools

* Java 11 / Kotlin
* Maven 3
* Docker

## Kääntäminen ja ajaminen

Projekti kääntyy suorittamalla `mvn clean install`.

Paikallinen kehitysympäristö on pystytettävissä kääntämisen jälkeen docker-composella:
`docker-compose up -d`. Tämä pystyttää vtj-servicen ja vtj-test-ws:n erillisiin kontteihin, käyttäen
devauskonfiguraatioita (ks. DEV-security-context-backend.xml ja DEV-vtj-service.properties vtj-service -hakemistossa).

Kummankin palvelun pyöriessä kyselyjä voi tehdä osoitteeseen http://localhost:8080/vtj-service/resources/vtj/<hetu> -
sopivia hetuja löytyy vtj-servicen migraatiotiedostoista. Vaaditun autentikoinnin (basic auth) tunnus ja salasana ovat
kaivettavissa em. konffitiedostoista.

Pelkkää `vtj-test-ws`:ää voi ajaa Mavenilla sen omasta hakemistolla: `mvn spring-boot:run`.


## Konfigurointi

Itse VTJ-serviceä konfiguroidaan samaan tapaan, kuin valtaosaa projekteista: sen Spring-konfiguraatioissa viitataan
property placeholdereihin, joiden arvot otetaan templaatin pohjalta luotavasta tiedostosta. Ks. tiedosto
`vtj-service.properties.template` juuriprojektin resourcesissa. Templaattiin tuodaan muuttujat ympäristön
`opintopolku.yml`:stä, sekä AWS:n salaisuuksista.

Spring Bootilla toteutettu `vtj-test-service` käyttää `application.yml`:ssä placeholdereita, joiden arvot luetaan
`Environment`:istä, johon on yhdistetty myös em. `vtj-service.properties`. Ks. `VtjPropertiesEnvironmentPostProcessor`.

### SSL ja sertifikaattiautentikaatio

Tuotannossa VTJ:n web service vaatii SSL-salauksen, sekä sertifikaattiautentikaation. Lokaalisti ajaessa ne ovat
oletusarvoisesti käytössä, mutta testiympäristöissä eivät. Ks. `docker-compose.yml` ja `vtj-test-ws/pom.xml`.


### Oppijanumerorekisteri-integraatio

Web servicen voi asettaa "ONR-läpilukutilaan", jolloin se etsii oman tietokantansa lisäksi henkilöitä
oppijanumerorekisteristä. Esimerkkejä ja toimintalogiikka: ks. `.env` ja `HenkiloService`.


### Debuggaaminen

Kontissa ajaessa debuggaus vaatii `JAVA_TOOL_OPTIONS` -ympäristömuuttujan asettamista, sekä siinä määritellyn portin
avaamista. Esimerkki poiskommentoituna `docker-compose.yml`:issä.

# VTJ-service

Palvelu henkilötietojen kyselemiseen väestötietojärjestelmästä. Koostuu seuraavista moduleista:
* vtj-api - rajapinnan palauttamat luokat
* vtj-remote-api - VTJ:n webservicen määrittely, siitä generoidut luokat ja client
* vtj-service - palvelurajapinta kyselyjen tekemiseen
* vtj-test-ws - webservice-toteutus testiympäristöihin

## Kääntäminen ja ajaminen

Projekti kääntyy suorittamalla `mvn clean install`.

Paikallinen kehitysympäristö on pystytettävissä kääntämisen jälkeen docker-composella:
`docker-compose up -d`. Tämä pystyttää vtj-servicen ja vtj-test-ws:n erillisiin kontteihin, käyttäen
devauskonfiguraatioita (ks. DEV-security-context-backend.xml ja DEV-vtj-service.properties vtj-service -hakemistossa).

Kummankin palvelun pyöriessä kyselyjä voi tehdä osoitteeseen http://localhost:8080/vtj-service/resources/vtj/<hetu> -
sopivia hetuja löytyy vtj-servicen migraatiotiedostoista. Vaaditun autentikoinnin (basic auth) tunnus ja salasana ovat
kaivettavissa em. konffitiedostoista.

Pelkkää `vtj-test-ws`:ää voi ajaa Mavenilla sen omasta hakemistolla: `mvn spring-boot:run`.

## SSL ja sertifikaattiautentikaatio

Tuotannossa VTJ:n web service vaatii SSL-salauksen, sekä sertifikaattiautentikaation. Lokaalisti ajaessa ne ovat
oletusarvoisesti käytössä, mutta testiympäristöissä eivät. Ks. `docker-compose.yml` ja `vtj-test-ws/pom.xml`.


## Oppijanumerorekisteri-integraatio

Web servicen voi asettaa "ONR-läpilukutilaan", jolloin se etsii oman tietokantansa lisäksi henkilöitä oppijanumerorekisteristä. Ks. `.env` ja `HenkiloService`.


## Debuggaaminen

Kontissa ajaessa debuggaus vaatii `JAVA_TOOL_OPTIONS` -ympäristömuuttujan asettamista, sekä siinä määritellyn portin avaamista. Ks. `docker-compose.yml`.

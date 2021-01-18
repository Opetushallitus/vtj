# VTJ-service

Palvelu henkilötietojen kyselemiseen väestötietojärjestelmästä. Koostuu seuraavista moduleista:
* vtj-api - rajapinnan palauttamat luokat
* vtj-remote-api - VTJ:n webservicen määrittely, siitä generoidut luokat ja client
* vtj-service - palvelurajapinta kyselyjen tekemiseen
* vtj-test-ws - webservice-toteutus testiympäristöihin

## Kääntäminen ja ajaminen

Projekti kääntyy suorittamalla `mvn clean install`.

Paikallinen kehitysympäristö on pystytettävissä kääntämisen jälkeen docker-composella: `docker-compose up -d`. Tämä pystyttää vtj-servicen ja vtj-test-ws:n erillisiin kontteihin, käyttäen devauskonfiguraatioita (ks. DEV-security-context-backend.xml ja DEV-vtj-service.properties vtj-service -hakemistossa).

Kummankin palvelun pyöriessä kyselyjä voi tehdä osoitteeseen http://localhost:8080/vtj-service/resources/vtj/<hetu> - sopivia hetuja löytyy vtj-servicen migraatiotiedostoista. Vaaditun autentikoinnin (basic auth) tunnus ja salasana ovat kaivettavissa em. konffitiedostoista.


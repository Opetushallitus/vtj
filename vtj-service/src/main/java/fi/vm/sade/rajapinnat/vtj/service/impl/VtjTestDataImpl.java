package fi.vm.sade.rajapinnat.vtj.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fi.vm.sade.rajapinnat.vtj.NotFoundException;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo;
import fi.vm.sade.rajapinnat.vtj.api.YksiloityHenkilo.Sukupuoli;
import fi.vm.sade.rajapinnat.vtj.service.VtjTestData;

public class VtjTestDataImpl implements VtjTestData{

    private static Random rand = new Random();
    private static int counter = 0;
    private static final List<YksiloityHenkilo> testData = new ArrayList<YksiloityHenkilo>();
    static {
        //hetu, etunimet, kutsumanimi, sukunimi, sukupuoli, random, sähköposti, katuosoiteS, katuosoiteR, postinumero, kaupunkiS, kaupunkiR, maaS, maaR
        testData.add(createNewYH("010156-9994", "Tobias Nikolas",              null,       "Siltanen",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010547-9991", "Milo Ingvald Rami",           null,       "Reponen",           Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010661-999E", "Veeti Valtteri",              null,       "Seppä",             Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010673-998H", "Meiju Sanna-Maria Taika",     null,       "Junnila",           Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010745-9983", "Claudia Heidi",               null,       "Gustafsson",        Sukupuoli.NAINEN, false, null, "Mannerheimintie 45 A 12", "Mannerheiminväg  45 A 12", "00101", "Helsinki", "Helsingfors", "Suomi", "Finland", "246"));
        testData.add(createNewYH("010849-999Y", "Joel Niila",                  null,       "Ahonen",            Sukupuoli.MIES, false, null, "Mannerheimintie 45 A 22", null, "00100", "Helsinki", null, "Suomi", null, "246"));
        testData.add(createNewYH("010873-9973", "Felix Jonathan",              null,       "Haapakoski",        Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010957-998T", "Vickan",                      null,       "Tuulispää",         Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010973-999Y", "Kaija",                       null,       "Tuulispää",         Sukupuoli.MIES, false, null, "Mannerheimintie 45 A 22", "Mannerheiminväg  45 A 22", "00101", "Helsinki", "Helsingfors", "Suomi", "Finland", "246"));
        testData.add(createNewYH("010973-999Y", "Joni Kaj",                    null,       "Hiltunen",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("011066-998X", "Maija Evi",                   null,       "Puumalainen",       Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("011073-998R", "Silja",                       null,       "Loikkanen",         Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("020637-998V", "Jane Kukka-Maaria",           null,       "Putkonen",          Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("030668-998H", "Aliisa Lumia",                "Lumia",    "Pietikäinen",       Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("031042-9981", "Isabelle Riia",               null,       "Kivimäki",          Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("031173-999V", "Ismo",                        null,       "Neuvonen",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("031251-9990", "Mohamed Mohamed Mustafa",     null,       "Lindroos",          Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("040779-998T", "Pihla Kukka-Maria",           null,       "Haapakoski",        Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("040946-999X", "Simeon Samuli",               null,       "Aarnio",            Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("050654-9997", "Helge Abel",                  null,       "Kiviniemi",         Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("080104A9997", "Eric Folke",                  null,       "Männikkö",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("130139-998E", "Ulla Rea",                    null,       "Pekkala",           Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("140977-998B", "Svetlana Päivi",              "Päivi",     "Männikkö",         Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("170479-999T", "Hans",                        null,       "Ollila",            Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("201100A9990", "Elias Kristian",              null,       "Männikkö",          Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("251102A996M", "Eriika Mailis",               null,       "Männikkö",          Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("260991-999R", "Ilmari Folke",                null,       "Huhtiistinen",      Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("261148-9987", "Sanelma Alice",               null,       "Ruuska Malila",     Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("270569-998E", "Kielo Janica",                null,       "Pietikäinen",       Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("040188-999A", "Ali Julian Fjalar",           null,       "Marttila",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("050351-9980", "Maarita Alicia",              null,       "Holmberg",          Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("061191-998D", "Aliisa Maiju Rebecca",        null,       "Kujala",            Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("070356-999L", "Mikke Samuli",                null,       "Laaksonen",         Sukupuoli.MIES, false, null, "Mannerheimintie 45A 33", "Mannerheiminväg 45 A 33", "00100", "Helsinki", "Helsingforss", "Suomi", "Finland", "246"));
        testData.add(createNewYH("160279-999J", "Matti Eemeli",                "Eemeli",    "Meikäläinen",      Sukupuoli.MIES, false, null, "Mannerheimintie 45A 44", null, "00100", "Helsinki", null, "Suomi", null, "246"));
        testData.add(createNewYH("160498-9992", "Vili Jimmy",                  null,       "Saukkonen",         Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("171047-999C", "Allan Kent",                  null,       "Minkkinen",         Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("191148-999R", "Dmitri Jonathan",             "Jonathan",  "Kataja",           Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("200470-998C", "Nella Pihla-Roosa",           null,       "Junnila",           Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("201171-999A", "Elmo",                        null,       "Andersson",         Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("220576-9992", "Ali Allan",                   null,       "Holopainen",        Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("230197-999W", "Gabriel Jalo Päiviö",         null,       "Pekkala",           Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("101019-998L", "Beatrice Kukka-Maaria",       null,       "Hirvelä",          Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("290154-999N", "Erkki Taavetti Valtteri",     null,       "Hyvönen",           Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("301275-998K", "Sulevi Kukka-Maaria Maiju",   null,       "Männikkö",          Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("250589-999B", "Denis Usko",                  null,       "Järveläinen",       Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("050369-999N", "Jalmari Elof Pontus",         null,       "Setälä",            Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("020894-9986", "Agneta Kukka-Maaria",         null,       "Saukkonen",         Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("291281-999H", "Dennis Kaj",                  null,       "Koljonen",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("150151-998U", "Maj Evi",                     null,       "Lindell",           Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("251199-999S", "Eric Andrew",                 "Andrew",    "Pettersson",      Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("250678-999A", "Waldemar Olli-Pekka Andres ", "Andres",    "Haanpää",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("181003A999N", "Ronny Bengt Thomas",          "Bengt",     "Setälä",           Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("150478-9987", "Minja Ella Nenna",            "Ella",      "Kolari",           Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("081078-999K", "Holger Raineri Kaino",        "Raineri",   "Soikkeli",        Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("050815-998R", "Lemmikki Alise",             "Alise",     "Ronkainen",         Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("041248-999V", "Bengt Valtteri",              "Valtteri",  "Koskela",         Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("230896-9988", "Sofi Ira Kukka-Maaria",       "Ira",       "Lyytinen",        Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("270474-999Y", "Robert Yrjö",                 "Yrjö",      "Hannula",         Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("160582-998P", "Lahja Nicole",                "Nicole",    "Kontkanen ",       Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("110360-9986", "Maija Aniitta",               "Aniitta",   "Hurskainen",       Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("271062-999P", "Pekka Joel",                 "Joel",      "Rautiainen ",       Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("240984-999M", "Rikhard Niila",               "Niila",     "Huusko",           Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("130486-9980", "Linda Neea",                 "Neea",      "Kuusinen",          Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("090369-9998", "Lenni Håkan",                 "Håkan",     "Kaukonen",         Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("090271-998X", "Meiju Maiju",                 "Maiju",     "Haapakoski",      Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010449-998P", "Sonja Agneta/Aune Kukka",     null,       "von Braun de Karttunen", Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010522-998J", "Elina Kukka-Maaria",          null,       "Rossi",             Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("150894-9877", "Teppo",                       "Teppo",    "Testaaja",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("150894-967K", "Seppo",                       "Seppo",    "Testaaja",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        //Vetuma
        testData.add(createNewYH("010101-123N", "Teemu",                       "Teemu",    "Testaaja",          Sukupuoli.MIES, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("210281-9988", "NORDEA",                      "NORDEA",   "DEMO",              Sukupuoli.NAINEN, false,null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("081181-9984", "ANNA",                        "ANNA",     "TESTI",             Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        testData.add(createNewYH("010170-960F", "Maija",                       "Maija",    "Meikäläinen",       Sukupuoli.NAINEN, false, null, null, null, null, null, null, null, null, "246"));
        //VTJ-update-tests static
        testData.add(createNewYH("020260-909P", "Matti",                       "Matti",    "Testinen",          Sukupuoli.MIES, false, "matti.meikalainen@fromvtj.oph", "Keskikatu 100", "Central gatan 100", "98765", "Pohjanmaa", "Österbotnia", "Suomi", "Finland", "246"));
        testData.add(createNewYH("020260-9833", "Ville",                       null,       "Meikäläinen",       Sukupuoli.MIES, false, "ville.meikalainen@fromvtj.oph", "Uusikatu 100", "Nya gatan 100", "12345", "Auramaa", "Åraland", "Suomi", "Finland", "246"));
        testData.add(createNewYH("020260-941R", "Teppo Seppo",                 "Teppo",    "Meikäläinen",       Sukupuoli.MIES, false, "seppo.meikalainen@fromvtj.oph", "Vanhakatu 100", "Gamla gatan 100", "54321", "Kymimaa", "Kymilandet", "Suomi", "Finland", "246"));
        testData.add(createNewYH("020260-961C", "Matti Seppo",                 "Seppo",    "Matikainen",        Sukupuoli.MIES, false, "seppo.matikainen@fromvtj.oph", "Vanhakatu 200", "Gamla gatan 200", "56321", "Hämemaa", "Hämelandet", "Suomi", "Finland", "246"));
    }

    private static String[] changingDataHetus = {"010150-969L", "010150-913T", "010150-979X"};
    private static String[] etunimet = {"Olavi Uolevi", "Eetu Aatu"};
    private static String[] sukunimet = {"Hakkarainen", "Tikkanen"};
    private static String[] katuosoitteet = {"Vanhakatu 1", "Keskikatu 2", "Uusikatu 3"};
    private static String[] kaupungit = {"Hesa", "Manse", "Suomen Chicago"};
    
    public YksiloityHenkilo teeHakuTestidatasta(String hetu) {
        for (YksiloityHenkilo yh : testData) {
            if (yh.getHetu().equalsIgnoreCase(hetu)) {
                YksiloityHenkilo result = new YksiloityHenkilo();
                result.setEtunimi(yh.getEtunimi());
                result.setSukunimi(yh.getSukunimi());
                result.setKutsumanimi(yh.getKutsumanimi());
                result.setHetu(yh.getHetu());
                result.setSukupuoli(yh.getSukupuoli());
                result.setSahkoposti(yh.getSahkoposti());
                if (yh.getOsoitteet() != null) {
                    for (YksiloityHenkilo.OsoiteTieto osoite : yh.getOsoitteet()) {
                        result.addOsoiteTieto(osoite);
                    }
                }
                return result;
            }
        }
        for(String changingDatahetu : changingDataHetus) {
            if (hetu.matches(changingDatahetu)) {
                return createNewYH(hetu, null, null, null, Sukupuoli.MIES, true, null, null, null, null, null, null, "Suomi", "Finland");
            }
        }
        // Vastauksena pitää aina löytyä henkilö tai palauttaa virhe!!
        throw new NotFoundException("Could not find person.");
    }

    private static YksiloityHenkilo createNewYH(String hetu,
                                          String etunimi,
                                          String kutsumanimi,
                                          String sukunimi,
                                          Sukupuoli sukupuoli,
                                          boolean random,
                                          String sahkoposti,
                                          //Osoitetieto
                                          String katuosoiteS,
                                          String katuosoiteR,
                                          String postinumero,
                                          String kaupunkiS,
                                          String kaupunkiR,
                                          String maaS,
                                          String maaR,
                                          String kansalaisuusKoodi
                                          ) {
        YksiloityHenkilo yh = new YksiloityHenkilo();

        if (random) {
            //Names applied in a sequence to make things more predictable
            yh.setEtunimi(etunimet[counter % 2]);
            yh.setSukunimi(sukunimet[counter % 2]);
            counter++;
            yh.setKutsumanimi(yh.getEtunimi().split(" ")[randInt(0,1)]);
            yh.addOsoiteTieto(yh.new OsoiteTieto("yhteystietotyyppi4",
                                                 katuosoitteet[randInt(0,2)],
                                                 katuosoitteet[randInt(0,2)],
                                                 new Integer(randInt(10000, 20000)).toString(),
                                                 kaupungit[randInt(0,2)],
                                                 kaupungit[randInt(0,2)],
                                                 maaS,
                                                 maaR));
            yh.setSahkoposti((yh.getKutsumanimi() + "@fromvtj.oph").toLowerCase());
            yh.addKansalaisuusKoodi(kansalaisuusKoodi);
        }
        else {
            yh.setEtunimi(etunimi);
            yh.setSukunimi(sukunimi);
            yh.setKutsumanimi(kutsumanimi);
            yh.addKansalaisuusKoodi("246");
            if (katuosoiteS != null || postinumero != null || kaupunkiS != null || maaS != null ||
                    katuosoiteR != null || kaupunkiR != null || maaR != null) {
                yh.addOsoiteTieto(yh.new OsoiteTieto("yhteystietotyyppi4",
                                                      katuosoiteS,
                                                      katuosoiteR,
                                                      postinumero,
                                                      kaupunkiS,
                                                      kaupunkiR,
                                                      maaS,
                                                      maaR));
            }
            yh.setSahkoposti(sahkoposti);
        }
        yh.setHetu(hetu);
        yh.setSukupuoli(sukupuoli);
        return yh;
    }

    private static int randInt(int min, int max) {
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}

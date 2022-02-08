-- Testihenkilöitä suomi.fi palveluhallinan dokumentaation mukaisesti (https://palveluhallinta.suomi.fi/fi/tuki/artikkelit/5a82ef7ab03cdc41de664a2b)

INSERT INTO henkilo (henkilotunnus, etunimet, kutsumanimi, sukunimi, sukupuoli, sahkoposti, kansalaisuus, kotimainen_osoite_id) VALUES
  ('010280-952L', 'Tessa',              null,       'Testilä ',          'NAINEN', null, '246', null),
  ('010469-999W', 'Joni Kristian',              null,       'Kivi',          'MIES', null, '246', null),
  ('010675-9981', 'Anna-Liisa Milka',           null,       'Sallinen',           'NAINEN', null, '246', null),
  ('261298-998X', 'Kukka-Maaria Jennika Etel',              null,       'Haapakoski-Henriksson',             'NAINEN', null, '246', null),
  ('040265-9985', 'Minerva Alli Aniitta',     null,       'Marttila',           'NAINEN', null, '246', null),
  -- Existing ('260991-999R', 'Ilmari Folke',               null,       'Huhtiistinen',        'MIES', null, '246', (SELECT id FROM kotimainen_osoite WHERE katu_fi = 'Mannerheimintie' AND katunumero = 45 AND porraskirjain = 'A' AND huoneistonumero = 12)),
  ('020365-999P', 'Valtter Niila',                  null,       'Grönholm',            'MIES', null, '246', (SELECT id FROM kotimainen_osoite WHERE katu_fi = 'Mannerheimintie' AND katunumero = 45 AND porraskirjain = 'A' AND huoneistonumero = 22 AND postinumero = '00100')),
  ('110106A998M', 'Anni Joanna',                  null,       'Johansson',            'NAINEN', null, '246', (SELECT id FROM kotimainen_osoite WHERE katu_fi = 'Mannerheimintie' AND katunumero = 45 AND porraskirjain = 'A' AND huoneistonumero = 22 AND postinumero = '00100')),
  ('290574-9981', 'Sinna',           null,       'Sukunimetön',           'NAINEN', null, '246', null),
  ('050391-999B', 'Äåöàáâãæçð Èèéêë-Ììíîïñòóôõ Øøßþùúûüýÿ',           null,       'Parkkonen',           'MIES', null, '246', null),
  ('120997-9998', 'Timo',           null,       'Tuulispää',           'MIES', null, '246', null),
  ('271258-9988', 'Ramona Ulla',           null,       'Tuulispää',           'NAINEN', null, '246', null),
  -- Existing ('010170-960F', 'Anna Ännä',           null,       'Demo',           'NAINEN', null, '246', null),
  ('010170-999R', 'Tero',           null,       'Testaaja',           'MIES', null, '246', null),
  -- Existing ('081181-9984', 'Sylvi Sofie',           null,       'Marttila',           'NAINEN', null, '246', null),
  ('101080-950T', '101080',           null,       'Testihenkilö ',           'MIES', null, '246', null),
  ('180883-998N', 'Maila Eleonora',                 NULL,    'Aaltonen',        'NAINEN', null, '246', (SELECT id FROM kotimainen_osoite WHERE katu_fi = 'Vanhakatu' AND katunumero = 200)),
  -- TURVAKIELTOINEN ONR:SSÄ
  ('191281-982H', 'Saima Testi',           null,       'Vauhkonen-Testi ',           'MIES', null, '246', null);

-- KOSKEA varten testitapaus, jossa huollettavalla on turvakielto ONR:ssä
INSERT INTO henkilo_huoltaja (huoltaja_id, huollettava_id) VALUES
  ((SELECT id FROM henkilo WHERE henkilotunnus = '010469-999W'), (SELECT id FROM henkilo WHERE henkilotunnus = '191281-982H'));

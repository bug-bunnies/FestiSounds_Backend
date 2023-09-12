-- Add 5 test festivals to the "festival" table

-- Festival 1
INSERT INTO festival (name, start_date, end_date, details, location, is_robbie_invited, image, festival_organizer)
VALUES ('Summer Music Festival', '2023-06-15', '2023-06-17', 'Three days of live music and fun in the sun.',
        'Beachfront Park', true, 'summer_music_festival.jpg', 'Music Events Inc.');

-- Festival 2
INSERT INTO festival (name, start_date, end_date, details, location, is_robbie_invited, image, festival_organizer)
VALUES ('RockFest 2023', '2023-07-20', '2023-07-23', 'The biggest rock festival of the year!', 'Rockville Arena', true,
        'rockfest_2023.jpg', 'Rock Productions LLC');

-- Festival 3
INSERT INTO festival (name, start_date, end_date, details, location, is_robbie_invited, image, festival_organizer)
VALUES ('Jazz on the Green', '2023-08-05', '2023-08-07', 'Smooth jazz under the stars.', 'City Park', false,
        'jazz_on_the_green.jpg', 'Jazz Nights');

-- Festival 4
INSERT INTO festival (name, start_date, end_date, details, location, is_robbie_invited, image, festival_organizer)
VALUES ('EDM Extravaganza', '2023-09-10', '2023-09-12', 'Electronic dance music all weekend long.', 'Electro Dome',
        true, 'edm_extravaganza.jpg', 'EDM Events Worldwide');

-- Festival 5
INSERT INTO festival (name, start_date, end_date, details, location, is_robbie_invited, image, festival_organizer)
VALUES ('Country Fest 2023', '2023-10-15', '2023-10-17', 'A celebration of country music in the heart of Texas.',
        'Texas Ranch', false, 'country_fest_2023.jpg', 'Country Vibes LLC');

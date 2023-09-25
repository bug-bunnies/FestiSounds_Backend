INSERT INTO festivals(uuid, name, start_date, end_date, details, city, country, is_robbie_invited, image, created_on,
                      last_updated_on, organizer, website)
VALUES (UUID_GENERATE_V4(), 'Fest1', TIMESTAMP '2023-09-26 14:00:00', TIMESTAMP '2023-09-27 22:00:00',
        'Details for Fest1', 'City1', 'Country1', TRUE, 'https://example.com/image1.jpg',
        TIMESTAMP '2023-09-25 13:00:00', TIMESTAMP '2023-09-25 13:00:00', 'Organizer1', 'https://fest1.com'),
       (UUID_GENERATE_V4(), 'Fest2', TIMESTAMP '2023-10-01 14:00:00', TIMESTAMP '2023-10-02 22:00:00',
        'Details for Fest2', 'City2', 'Country2', FALSE, 'https://example.com/image2.jpg',
        TIMESTAMP '2023-09-25 13:00:00', TIMESTAMP '2023-09-25 13:00:00', 'Organizer2', 'https://fest2.com'),
       (UUID_GENERATE_V4(), 'Fest3', TIMESTAMP '2023-10-06 14:00:00', TIMESTAMP '2023-10-07 22:00:00',
        'Details for Fest3', 'City3', 'Country3', TRUE, 'https://example.com/image3.jpg',
        TIMESTAMP '2023-09-25 13:00:00', TIMESTAMP '2023-09-25 13:00:00', 'Organizer3', 'https://fest3.com'),
       (UUID_GENERATE_V4(), 'Fest4', TIMESTAMP '2023-10-11 14:00:00', TIMESTAMP '2023-10-12 22:00:00',
        'Details for Fest4', 'City4', 'Country4', FALSE, 'https://example.com/image4.jpg',
        TIMESTAMP '2023-09-25 13:00:00', TIMESTAMP '2023-09-25 13:00:00', 'Organizer4', 'https://fest4.com')

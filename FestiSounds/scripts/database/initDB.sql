CREATE TABLE IF NOT EXISTS festivals
(
    id                 UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name               VARCHAR(100) NOT NULL,
    start_date         DATE         NOT NULL,
    end_date           DATE         NOT NULL,
    details            VARCHAR(500) NOT NULL,
    location           VARCHAR(100) NOT NULL,
    is_robbie_invited  BOOLEAN,
    image              VARCHAR(500) NOT NULL,
    created_on         TIMESTAMPTZ      DEFAULT current_timestamp,
    last_updated_on    TIMESTAMPTZ      DEFAULT current_timestamp,
    festival_organizer VARCHAR(100) NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_festival_id ON festivals (id);

CREATE TABLE festival_artist (
     id UUID PRIMARY KEY DEFAULT,
     spotify_id VARCHAR(100) NOT NULL UNIQUE,
     artist_name VARCHAR(100) NOT NULL
);

CREATE TABLE festival_artist_festival (
      spotify_id VARCHAR(100),
      festival_id UUID,
      FOREIGN KEY (spotify_id) REFERENCES festival_artist(spotify_id),
      FOREIGN KEY (festival_id) REFERENCES festivals(id)
);
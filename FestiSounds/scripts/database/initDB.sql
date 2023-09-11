CREATE TABLE IF NOT EXISTS festival
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

CREATE INDEX IF NOT EXISTS idx_festival_id ON festival (id);
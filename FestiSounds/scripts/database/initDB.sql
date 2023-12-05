create extension if not exists "uuid-ossp";

create table if not exists
    festivals
(
    uuid              uuid        default uuid_generate_v4() primary key,
    name              text                     not null,
    start_date        timestamp with time zone not null,
    end_date          timestamp with time zone not null,
    details           text                     not null,
    city              text                     not null,
    country           text                     not null,
    is_robbie_invited boolean,
    image             text,
    created_on        TIMESTAMPTZ default current_timestamp,
    last_updated_on   TIMESTAMPTZ default current_timestamp,
    organizer         text                     not null,
    website           text                     not null
);

create table
    festival_artist
(
    id          uuid default uuid_generate_v4() primary key,
    spotify_id  text not null,
    artist_name text not null
);

create table
    festival_artist_link
(
    festival_artist_id uuid references festival_artist (id),
    festival_id        uuid references festivals (uuid),
    primary key (festival_artist_id, festival_id)
);
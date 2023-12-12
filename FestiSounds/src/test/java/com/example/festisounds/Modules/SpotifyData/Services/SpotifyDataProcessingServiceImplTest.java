package com.example.festisounds.Modules.SpotifyData.Services;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test processing of the spotify data")
class SpotifyDataProcessingServiceImplTest {

    ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void correctGenreRankingCreatedFor6ArtistDataset() throws IOException {

        InputStream artistInputStream = Artist[].class.getResourceAsStream("/DataForSixArtists.txt");

        Artist[] test6Artists = mapper.readValue(artistInputStream, Artist[].class);
        System.out.println(test6Artists[0].getName());

    }
}
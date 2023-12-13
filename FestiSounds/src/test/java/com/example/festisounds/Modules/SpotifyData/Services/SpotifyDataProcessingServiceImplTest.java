package com.example.festisounds.Modules.SpotifyData.Services;

import com.example.festisounds.Modules.SpotifyData.DTOs.TopItemsDTO;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.hc.core5.http.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test processing of the spotify data")
//@SpringBootTest
class SpotifyDataProcessingServiceImplTest {

    SpotifyDataServiceImpl dataService = new SpotifyDataServiceImpl();
    SpotifyDataProcessingServiceImpl service = new SpotifyDataProcessingServiceImpl(dataService);
//    public SpotifyDataProcessingServiceImplTest(SpotifyDataProcessingServiceImpl spotifyDataProcessingServiceImpl) {
//        this.spotifyDataProcessingServiceImpl = spotifyDataProcessingServiceImpl;
//    }

    ObjectMapper mapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    @Test
    public void correctGenreRankingCreatedFor6ArtistDataset() throws IOException {

        InputStream artistInputStream = Artist[].class.getResourceAsStream("/DataForSixArtists.txt");
        Artist[] test6Artists = mapper.readValue(artistInputStream, Artist[].class);

        HashMap<String, Double> result = service.generateGenreRanking(test6Artists);
        System.out.println(result);

    }
}
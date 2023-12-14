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
        assertEquals(13, result.size(), "Genre ranking map has wrong number of genres");
        assertEquals(28.57, Math.round(100 * result.get("modern folk rock")) /100.0, "Genre 'modern folk rock' does not have expected score of 28.57");
        assertEquals(28.57, Math.round(100 * result.get("neo mellow")) /100.0, "Genre 'neo mellow' does not have expected score of 28.57");
        assertEquals(23.81, Math.round(100 * result.get("pop")) /100.0, "Genre 'pop' does not have expected score of 23.81");
        assertEquals(19.05, Math.round(100 * result.get("canadian indie")) /100.0, "Genre 'canadian indie' does not have expected score of 19.05");
    }
}
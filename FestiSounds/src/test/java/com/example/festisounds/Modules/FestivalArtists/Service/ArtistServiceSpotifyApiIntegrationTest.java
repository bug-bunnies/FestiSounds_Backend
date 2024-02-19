package com.example.festisounds.Modules.FestivalArtists.Service;

import com.example.festisounds.FestiSoundsApplication;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistRequestDTO;
import com.example.festisounds.Modules.FestivalArtists.Repositories.FestivalArtistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import se.michaelthelin.spotify.model_objects.specification.Artist;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FestiSoundsApplication.class})
@ContextConfiguration
public class ArtistServiceSpotifyApiIntegrationTest {
    @Autowired
    ArtistService artistService;

    @Disabled
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = { "tom odell", "pawsa", "the lumineers", "avicii" })
    @DisplayName("Filter out right data from spotify")
    void findArtistInSpotifyAndCreateArtistObject_whenGivenArtistArray_returnsCorrectData(String name) {
        Artist[] foundArtists = artistService.getSpotifyArtistData(name);

        ArtistRequestDTO artist = artistService.findArtistInSpotifyAndCreateArtistObject(name, foundArtists);

        Assertions.assertNotNull(artist, () -> "Artist should exist");
        Assertions.assertEquals(foundArtists[0].getName(), name, () -> "Artist name does not match");
        Assertions.assertTrue(artist.genres().size() > 0, () -> "Artist should have at least one genre");

    }
}

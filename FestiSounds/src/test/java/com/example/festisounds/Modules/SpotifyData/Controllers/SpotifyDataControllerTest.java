package com.example.festisounds.Modules.SpotifyData.Controllers;

import com.example.festisounds.Modules.SpotifyData.Controller.SpotifyDataController;
import com.example.festisounds.Modules.SpotifyData.Services.SpotifyDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Followers;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpotifyDataController.class)
public class SpotifyDataControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SpotifyDataService spotifyDataService;

    Artist[] artists = new Artist[]{
            new Artist.Builder()
                    .setName("Fred again..")
                    .setExternalUrls(null)
                    .setFollowers(Followers(null, 0))
                    .setGenres("edm", "house", "stutter house")
                    .setHref("https://api.spotify.com/v1/artists/4oLeXFyACqeem2VImYeBFe")
                    .setId("4oLeXFyACqeem2VImYeBFe")
                    .setImages(null, null)
                    .setPopularity(75)
                    .setType(null)
                    .setUri("spotify:artist:4oLeXFyACqeem2VImYeBFe")
                    .build(),
            new Artist.Builder()
                    .setName("ACDC..")
                    .setExternalUrls(null)
                    .setFollowers(Followers(null, 0))
                    .setGenres("edm", "house", "stutter house")
                    .setHref("https://api.spotify.com/v1/artists/4oLeXFyACqeem2VImYeBFe")
                    .setId("4oLeXFyACqeem2VImYeBFe")
                    .setImages(null, null)
                    .setPopularity(75)
                    .setType(null)
                    .setUri("spotify:artist:4oLeXFyACqeem2VImYeBFe")
                    .build()
    };

    private Followers Followers(Object o, int i) {
        return null;
    }

    @Test
    void test1() throws Exception {
        given(spotifyDataService.getUsersTopArtists()).willReturn(artists);

        mockMvc.perform(get(SpotifyDataController.BASE_PATH_TOP_ARTISTS))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.[0].genres.length()", is(3)));
    }
}

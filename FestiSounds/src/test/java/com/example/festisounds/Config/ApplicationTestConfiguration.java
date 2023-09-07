package com.example.festisounds.Config;

import com.example.festisounds.Controller.SpotifyDataController;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Followers;



@TestConfiguration
public class ApplicationTestConfiguration {

    private Followers Followers(Object o, int i) {
        return null;
    }


    @Bean
    @Primary
    public SpotifyDataController mockedSpotifyDataController() {
        SpotifyDataController mockedSpotifyDataController = Mockito.mock(SpotifyDataController.class);
        Artist artist = new Artist.Builder()
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
                .build();

        Artist[] artists = new Artist[] {
                artist
        };

        Mockito.when(mockedSpotifyDataController.getUserTopArtists())
                .thenReturn(artists);
        return mockedSpotifyDataController;
    }


}

package com.example.festisounds;

import com.example.festisounds.Config.ApplicationTestConfiguration;
import com.example.festisounds.Controller.SpotifyDataController;
import com.example.festisounds.Service.SpotifyDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Followers;

import static org.junit.jupiter.api.Assertions.assertEquals;


@Import(ApplicationTestConfiguration.class)
@SpringBootTest
public class AuthControllerTests {

    @Autowired
    SpotifyDataService spotifyDataService;

    @Autowired
    SpotifyDataController spotifyDataController;
    // service uses the client

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

    private Followers Followers(Object o, int i) {
        return null;
    }

    @Test
    public void shouldReturnOneTopArtist(){
        Artist[] artists = spotifyDataController.getUserTopArtists();
        System.out.println(artists);
        assertEquals(artists, new Artist[] {artist});
//        assertThat(hero.name()).isEqualTo("Donald Duck");
//        assertThat(hero.films()).isEmpty();
    }
}

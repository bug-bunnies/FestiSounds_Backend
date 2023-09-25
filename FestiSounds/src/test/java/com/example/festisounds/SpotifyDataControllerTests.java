//package com.example.festisounds;
//
//import com.example.festisounds.Config.ApplicationTestConfiguration;
//import com.example.festisounds.Modules.SpotifyData.Services.SpotifyDataServiceImpl;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.context.annotation.Import;
//import se.michaelthelin.spotify.model_objects.specification.Artist;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//
//@Import(ApplicationTestConfiguration.class)
//@SpringBootTest
//public class SpotifyDataControllerTests {
//
//    @Autowired
//    SpotifyDataServiceImpl spotifyDataServiceImpl;
//
//    @Test
//    public void shouldMockGetUsersTopArtists(){
//        Artist[] artists = spotifyDataServiceImpl.getUsersTopArtists();
//        assertEquals(artists[0].getName(),"Fred again..");
//    }
//
//    @Test
//    public void shouldReturnCorrectGenreValues(){
//        Map<String, Long> genres = spotifyDataServiceImpl.userTopGenres();
//        assertEquals(3, genres.size());
//        assertTrue(genres.containsKey("house"));
//    }
//
//}

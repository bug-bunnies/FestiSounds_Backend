package com.example.festisounds.Modules.SpotifyData.Controller;

import com.example.festisounds.Modules.SpotifyData.Services.SpotifyDataService;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.util.Map;

@RestController
//@RequestMapping("/api/spotify")
public class SpotifyDataController {

    public static final String BASE_PATH_TOP_ARTISTS = "/api/spotify/user-top-artists";

    public static final String BASE_PATH_GET_GENRES = "/api/spotify/get-genres";

    private final SpotifyDataService SpotifyDataService;

    public SpotifyDataController(SpotifyDataService SpotifyDataService) {
        this.SpotifyDataService = SpotifyDataService;
    }

    //    @GetMapping(value = "user-top-artists")
    @GetMapping(BASE_PATH_TOP_ARTISTS)
    public ResponseEntity<Artist[]> getUserTopArtists() throws IOException, ParseException, SpotifyWebApiException {

        return ResponseEntity.ok(SpotifyDataService.getUsersArtistsForTimeframe("medium_term"));

    }

    //    @GetMapping(value = "get-genres")
//    @GetMapping(BASE_PATH_GET_GENRES)
//    public ResponseEntity<Map<String, Long>> getUserTopArtistsGenres() throws IOException, ParseException, SpotifyWebApiException {
//        Map<String, Long> genres = SpotifyDataService.userTopGenres();
//            return ResponseEntity.ok(genres);
//    }


}

package com.example.festisounds.Modules.SpotifyData.Controller;

import com.example.festisounds.Modules.SpotifyData.Services.SpotifyDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.Map;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyDataController {

    private final SpotifyDataService SpotifyDataService;

    public SpotifyDataController(SpotifyDataService SpotifyDataService) {
        this.SpotifyDataService = SpotifyDataService;
    }

    @GetMapping(value = "user-top-artists")
    public ResponseEntity<Artist[]> getUserTopArtists() {
        return ResponseEntity.ok(SpotifyDataService.getUsersTopArtists());
    }

    @GetMapping(value = "get-genres")
    public ResponseEntity<Map<String, Long>> getUserTopArtistsGenres() {
        Map<String, Long> genres = SpotifyDataService.userTopGenres();
            return ResponseEntity.ok(genres);
    }
}

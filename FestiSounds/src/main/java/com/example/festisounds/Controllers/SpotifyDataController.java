package com.example.festisounds.Controllers;

import com.example.festisounds.Services.ISpotifyDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.Map;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyDataController {

    private final ISpotifyDataService ISpotifyDataService;

    public SpotifyDataController(ISpotifyDataService ISpotifyDataService) {
        this.ISpotifyDataService = ISpotifyDataService;
    }

    @GetMapping(value = "user-top-artists")
    public ResponseEntity<Artist[]> getUserTopArtists() {
        return ResponseEntity.ok(ISpotifyDataService.getUsersTopArtists());
    }

    @GetMapping(value = "get-genres")
    public ResponseEntity<Map<String, Long>> getUserTopArtistsGenres() {
            Map<String, Long> genres = ISpotifyDataService.userTopGenres();
            return ResponseEntity.ok(genres);
    }
}

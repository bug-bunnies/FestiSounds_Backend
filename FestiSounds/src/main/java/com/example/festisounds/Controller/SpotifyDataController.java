package com.example.festisounds.Controller;

import com.example.festisounds.Service.ISpotifyDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.util.Arrays;
import java.util.Map;

import static com.example.festisounds.Controller.AuthController.spotifyApi;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyDataController {

    private final ISpotifyDataService ISpotifyDataService;

    public SpotifyDataController(ISpotifyDataService ISpotifyDataService) {
        this.ISpotifyDataService = ISpotifyDataService;
    }

    @GetMapping(value = "user-top-artists")
    public Artist[] getUserTopArtists() {
        return ISpotifyDataService.getUsersTopArtists();
    }

    @GetMapping(value = "get-genres")
    public ResponseEntity<Map<String, Long>> getTopArtistsGenres(){
        try {
            Map<String, Long> genres = ISpotifyDataService.userTopGenres();
            return ResponseEntity.ok(genres);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}

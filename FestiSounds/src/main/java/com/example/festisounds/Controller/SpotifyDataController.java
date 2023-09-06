package com.example.festisounds.Controller;

import com.example.festisounds.Service.SpotifyDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.util.Locale;
import java.util.Map;

import static com.example.festisounds.Controller.AuthController.spotifyApi;

@RestController
@RequestMapping("/api/spotify")
public class SpotifyDataController {

    private final SpotifyDataService spotifyDataService;

    public SpotifyDataController(SpotifyDataService spotifyDataService) {
        this.spotifyDataService = spotifyDataService;
    }

    @GetMapping(value = "user-top-artists")
    public Artist[] getUserTopArtists() {

        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                .time_range("medium_term")
                .limit(10)
                .offset(0)
                .build();

        try {
            final Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();

            return artistPaging.getItems();
        } catch (Exception e) {

            System.out.println("Something went wrong!\n" + e.getMessage());
        }
        return new Artist[0];
    }

    @GetMapping(value = "get-genres")
    public ResponseEntity<Map<String, Long>> getTopArtistsGenres(){
        try {
            Map<String, Long> genres = spotifyDataService.userTopGenres();
            return ResponseEntity.ok(genres);

        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }
}

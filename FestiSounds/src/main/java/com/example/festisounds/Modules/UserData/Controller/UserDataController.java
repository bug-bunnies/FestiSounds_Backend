package com.example.festisounds.Modules.UserData.Controller;

import com.example.festisounds.Modules.UserData.DTOs.SpotifyUserDataDTO;
import com.example.festisounds.Modules.UserData.Services.UserRequestService;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;

@RestController
public class UserDataController {

    public static final String BASE_PATH_TOP_ARTISTS = "/api/spotify/user-top-artists";
    private final UserRequestService userRequestService;


    public UserDataController(UserRequestService UserRequestService) {
        this.userRequestService = UserRequestService;
    }

    @GetMapping(BASE_PATH_TOP_ARTISTS)
    public ResponseEntity<Artist[]> getUserTopArtists() throws IOException, ParseException, SpotifyWebApiException {

        return ResponseEntity.ok(userRequestService.getUsersArtistsForTimeframe("medium_term"));

    }

    @GetMapping("/api/spotify/user-spotify-data")
    public ResponseEntity<SpotifyUserDataDTO> getUserSpotifyInfo() throws IOException, ParseException, SpotifyWebApiException {
        return ResponseEntity.ok(userRequestService.getUserSpotifyInfo());
    }
}

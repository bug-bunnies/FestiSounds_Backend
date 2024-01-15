package com.example.festisounds.Core.Controllers;

import com.example.festisounds.Modules.UserData.DTOs.SpotifyUserDataDTO;
import com.example.festisounds.Modules.UserData.Services.UserCachingServiceImpl;
import com.example.festisounds.Modules.UserData.Services.UserProcessingServiceImpl;
import com.example.festisounds.Modules.UserData.Services.UserRequestServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.SpotifyHttpManager;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;


@RestController
@RequestMapping("/api/")
public class AuthController {

    private static final String clientId = System.getenv("clientId");
    private static final String clientSecret = System.getenv("clientSecret");
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-user-code");
    private static String code = "";

    public static Integer expirationToken;

    @Autowired
    private UserProcessingServiceImpl userProcessingService;

    @Autowired
    private UserRequestServiceImpl userRequestService;
    @Autowired
    private UserCachingServiceImpl userCachingService;

    @Autowired
    CacheManager cacheManager;


    public static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();

    public static void refreshAccessToken() throws IOException, SpotifyWebApiException, ParseException {
        AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();
        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.execute();

        expirationToken = LocalDateTime.now().getSecond() + authorizationCodeCredentials.getExpiresIn();
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
    }

    @GetMapping("login")
    @ResponseBody
    public String spotifyLogin() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .scope("user-read-private, user-read-email, user-top-read")
                .show_dialog(true)
                .build();
        final URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    @GetMapping(value = "get-user-code")
    public String getSpotifyUserCode(@RequestParam("code") String userCode, HttpServletResponse response) throws IOException {
        code = userCode;
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code)
                .build();
        try {
            userCachingService.evictAllCaches();

            final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();

            expirationToken = LocalDateTime.now().getSecond() + authorizationCodeCredentials.getExpiresIn();

            // Set access and refresh token for further "spotifyApi" object usage
            spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
            spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());

            System.out.println("Spotify access token: " + spotifyApi.getAccessToken());
            System.out.println("Spotify refresh token: " + spotifyApi.getRefreshToken());
            System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());

//            TODO: Finish comparing data.
            HashMap<String, Double> genreData = userProcessingService.rankUsersFavouriteGenres();
            SpotifyUserDataDTO profileData = userRequestService.getUserSpotifyInfo();

            System.out.println("The cached house genre score: " + genreData.get("house"));
            System.out.println("The cached spotify user data is: " + profileData);

            Cache cachedArtists = cacheManager.getCache("user-top-artists");
            Cache cachedGenre = cacheManager.getCache("user-genre-data");
            Cache cachedUser = cacheManager.getCache("user-profile-data");

            HashMap<String, Double> genre = cachedGenre.get(new SimpleKey(), HashMap.class);
            SpotifyUserDataDTO user = cachedUser.get(new SimpleKey(), SpotifyUserDataDTO.class);


            System.out.println(genre.get("house") + " genre cache!");
            System.out.println(user.display_name() + " user cache!");
            System.out.println(cachedArtists);
            System.out.println(cachedArtists.getNativeCache());
            ArrayList<String> artistData = cachedArtists.get(new SimpleKey("cacheUserArtistData"), ArrayList.class);

            System.out.println("The cached artist list is: " + artistData.toString());


        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        response.sendRedirect("http://localhost:5173");
        return spotifyApi.getAccessToken();
    }
}

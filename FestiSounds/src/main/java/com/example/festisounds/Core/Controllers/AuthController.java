package com.example.festisounds.Core.Controllers;

import com.example.festisounds.Core.Utils.GenrePositionMapGenerator;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.UserData.DTOs.SpotifyUserDataDTO;
import com.example.festisounds.Modules.UserData.Services.UserArtistMatchingServiceImpl;
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
import java.util.LinkedHashMap;
import java.util.UUID;


@RestController
@RequestMapping("/api/")
public class AuthController {

    private static final String clientId = System.getenv("clientId");
    private static final String clientSecret = System.getenv("clientSecret");
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-user-code");
    private static String code = "";

    public static Integer expirationToken;

    private static UUID festivalId = UUID.fromString("8c14106e-a85c-4b7b-bcec-4803db825175");

    @Autowired
    private UserProcessingServiceImpl userProcessingService;

    @Autowired
    private UserArtistMatchingServiceImpl matchingService;

    @Autowired
    private UserRequestServiceImpl userRequestService;
    @Autowired
    private UserCachingServiceImpl userCachingService;
    private GenrePositionMapGenerator mapGenerator = new GenrePositionMapGenerator();

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
//            HashMap<String, Double> genreData = userProcessingService.rankUsersFavouriteGenres();
            Cache cachedGenreData = cacheManager.getCache("user-genre-data");
            System.out.println(cachedGenreData.getNativeCache() + " CACHE USER GENRE");
            HashMap<String, short[]> genrePositionMap = userCachingService.buildAndCacheGenrePositionMap("Genre3DMap2.csv");

            Cache cachedGenreMap = cacheManager.getCache("genre-position-data");
            HashMap<String, short[]> genrePositionMapCached = cachedGenreMap.get("Genre3DMap2.csv", HashMap.class);
            System.out.println(genrePositionMap + " OBJECT");
            System.out.println(cachedGenreMap.getNativeCache() + " native cache");
            System.out.println(genrePositionMapCached + " CACHE");


        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        response.sendRedirect("http://localhost:5173");
        return spotifyApi.getAccessToken();
    }
}

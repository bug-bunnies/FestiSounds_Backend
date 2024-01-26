package com.example.festisounds.Core.Controllers;

import com.example.festisounds.Core.Utils.GenrePositionMapGenerator;
import com.example.festisounds.Modules.UserData.Services.UserArtistMatchingServiceImpl;
import com.example.festisounds.Modules.UserData.Services.UserCachingServiceImpl;
import com.example.festisounds.Modules.UserData.Services.UserProcessingServiceImpl;
import com.example.festisounds.Modules.UserData.Services.UserRequestServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
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
import java.util.UUID;


@RestController
@RequestMapping("/api/")
public class AuthController {

    private static final String clientId = System.getenv("clientId");
    private static final String clientSecret = System.getenv("clientSecret");
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/api/get-user-code");
    public static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .build();
    private static final UUID festivalId = UUID.fromString("8c14106e-a85c-4b7b-bcec-4803db825175");
    public static Integer expirationToken;
    private static String code = "";

    @Autowired
    CacheManager cacheManager;
    @Autowired
    private UserProcessingServiceImpl userProcessingService;
    @Autowired
    private UserArtistMatchingServiceImpl matchingService;
    @Autowired
    private UserRequestServiceImpl userRequestService;
    @Autowired
    private UserCachingServiceImpl userCachingService;
    private final GenrePositionMapGenerator mapGenerator = new GenrePositionMapGenerator();

    @Value("${positionMap.location}")
    private String genrePositionMapFile;

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

            System.out.println("here 1");
            userProcessingService.rankUsersFavouriteGenres();
            System.out.println("here 2");
//            userRequestService.getUserSpotifyInfo();
            System.out.println("here 3");
            userCachingService.buildAndCacheGenrePositionMap(genrePositionMapFile);

            System.out.println("here 4");
            System.out.println(matchingService.getArtistRankingFromFestival(festivalId) + " algo results!");

        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
        response.sendRedirect("http://localhost:5173");
        return spotifyApi.getAccessToken();
    }
}

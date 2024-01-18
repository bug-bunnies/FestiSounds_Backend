package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Core.Controllers.AuthController;
import com.example.festisounds.Modules.UserData.DTOs.SpotifyUserDataDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopArtistsDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;
import se.michaelthelin.spotify.requests.data.users_profile.GetCurrentUsersProfileRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

import static com.example.festisounds.Core.Controllers.AuthController.spotifyApi;

@Service
public class UserRequestServiceImpl implements UserRequestService {

    @Autowired
    private UserCachingService cachingService;

    public static final int resultLimit = 50;

    @Override
    public TopArtistsDTO getUsersItems() throws IOException, ParseException, SpotifyWebApiException {
        if (AuthController.expirationToken > LocalDateTime.now().getSecond()) {
            AuthController.refreshAccessToken();
        }
        // TODO: Do these 3 calls in parallel eventually - can reduce number of calls if api calls become too expensive
        Artist[] topShortTermArtists = getUsersArtistsForTimeframe("short_term");
        Artist[] topMediumTermArtists = getUsersArtistsForTimeframe("medium_term");
        Artist[] topLongTermArtists = getUsersArtistsForTimeframe("long_term");
        TopArtistsDTO topArtists = new TopArtistsDTO(topShortTermArtists, topMediumTermArtists, topLongTermArtists);

        cachingService.cacheUserArtistData(topShortTermArtists, topMediumTermArtists, topLongTermArtists);

        return topArtists;
    }

    @Override
    public Artist[] getUsersArtistsForTimeframe(String timeframe) throws IOException, ParseException, SpotifyWebApiException {
        if (AuthController.expirationToken > LocalDateTime.now().getSecond()) {
            AuthController.refreshAccessToken();  // This line throws the exceptions in the method signature.
        }

        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                .time_range(timeframe)
                .limit(resultLimit)
                .build();

        try {
            Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            return artistPaging.getItems();
        } catch (Exception e) {
            // this exception handling probably needs looking at.
            throw new RuntimeException("Something went wrong getting top artists!\n" + e.getMessage());
        }
    }

    @Cacheable(value = "user-profile-data", key = "#root.method.name")
    @Override
    public SpotifyUserDataDTO getUserSpotifyInfo() throws IOException, ParseException, SpotifyWebApiException {
        GetCurrentUsersProfileRequest currentUsersProfileRequest = spotifyApi.getCurrentUsersProfile().build();
        var userInfo = currentUsersProfileRequest.execute();

        return new SpotifyUserDataDTO(userInfo.getId(), userInfo.getDisplayName(), Arrays.stream(userInfo.getImages()).findFirst().get().getUrl());
    }


}

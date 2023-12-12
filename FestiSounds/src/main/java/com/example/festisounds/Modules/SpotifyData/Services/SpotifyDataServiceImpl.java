package com.example.festisounds.Modules.SpotifyData.Services;

import com.example.festisounds.Core.Controllers.AuthController;
import com.example.festisounds.Modules.SpotifyData.DTOs.TopItemsTimeframeDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopTracksRequest;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.example.festisounds.Core.Controllers.AuthController.spotifyApi;

@Service
public class SpotifyDataServiceImpl implements SpotifyDataService {

    public static final int resultLimit = 50;
    @Override
    public TopItemsTimeframeDTO getUsersItems() throws IOException, ParseException, SpotifyWebApiException {
        if (AuthController.expirationToken > LocalDateTime.now().getSecond()) {
            AuthController.refreshAccessToken();
        }
        // Do these 3 calls in parallel eventually - can reduce number of calls if api calls become too expensive
        Artist[] topShortTermArtists = getUsersArtistsForTimeframe("short_term");
        Artist[] topMediumTermArtists = getUsersArtistsForTimeframe("medium_term");
        Artist[] topLongTermArtists = getUsersArtistsForTimeframe("long_term");

        Track[] topShortTermTracks = getUsersTracksForTimeframe("short_term");
        Track[] topMediumTermTracks = getUsersTracksForTimeframe("medium_term");
        Track[] topLongTermTracks = getUsersTracksForTimeframe("long_term");

        return new TopItemsTimeframeDTO(topShortTermArtists, topMediumTermArtists, topLongTermArtists, topShortTermTracks, topMediumTermTracks, topLongTermTracks);
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

    @Override
    public Track[] getUsersTracksForTimeframe(String timeframe) throws IOException, ParseException, SpotifyWebApiException {
        if (AuthController.expirationToken > LocalDateTime.now().getSecond()) {
            AuthController.refreshAccessToken();  // This line throws the exceptions in the method signature.
        }

        GetUsersTopTracksRequest getUsersTopTracksRequest = spotifyApi.getUsersTopTracks()
                .time_range(timeframe)
                .limit(resultLimit)
                .build();

        try {
            Paging<Track> trackPaging = getUsersTopTracksRequest.execute();
            return trackPaging.getItems();
        } catch (Exception e) {
            // this exception handling probably needs looking at.
            throw new RuntimeException("Something went wrong getting top tracks!\n" + e.getMessage());
        }
    }
}

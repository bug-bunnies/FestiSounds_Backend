package com.example.festisounds.Modules.SpotifyData.Services;

import com.example.festisounds.Core.Controllers.AuthController;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.festisounds.Core.Controllers.AuthController.spotifyApi;

@Service
public class SpotifyDataServiceImpl implements SpotifyDataService {

    @Override
    public Map<String, Long> userTopGenres() throws IOException, ParseException, SpotifyWebApiException {
        Artist[] usersTopArtists = getUsersTopArtists();
        System.out.println(usersTopArtists);
        return Arrays.stream(usersTopArtists)
                .flatMap(artist -> Arrays.stream(artist.getGenres()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }


    // This method can probably be removed.
    @Override
    public Artist[] getUsersTopArtists() throws IOException, ParseException, SpotifyWebApiException {
        if (AuthController.expirationToken > LocalDateTime.now().getSecond()) {
            AuthController.refreshAccessToken();
        }

        GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                .time_range("medium_term")
                .limit(10)
                .offset(0)
                .build();

        try {
            Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            return artistPaging.getItems();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong getting top artists!\n" + e.getMessage());
        }
    }

    @Override
    public Artist[] getUsersArtists() throws IOException, ParseException, SpotifyWebApiException {
        if (AuthController.expirationToken > LocalDateTime.now().getSecond()) {
            AuthController.refreshAccessToken();
        }
        // Do these 3 calls in parallel eventually - can reduce number of calls if api calls become too expensive
        GetUsersTopArtistsRequest getUsersArtistsShortTerm = spotifyApi.getUsersTopArtists()
                .time_range("short_term")
                .limit(50)
                .build();
        GetUsersTopArtistsRequest getUsersArtistsMediumTerm = spotifyApi.getUsersTopArtists()
                .time_range("medium_term")
                .limit(50)
                .build();
        GetUsersTopArtistsRequest getUsersArtistsLongTerm = spotifyApi.getUsersTopArtists()
                .time_range("long_term")
                .limit(50)
                .build();

        try {
            Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            return artistPaging.getItems();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong getting top artists!\n" + e.getMessage());
        }
        return new Artist[0];
    }

}

package com.example.festisounds.Service;

import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.festisounds.Controller.AuthController.spotifyApi;

@Service
public class SpotifyDataServiceImpl implements SpotifyDataService {

    @Override
    public Map<String, Long> userTopGenres() throws Exception {

        final GetUsersTopArtistsRequest getUsersTopArtistsRequest = spotifyApi.getUsersTopArtists()
                .time_range("medium_term")
                .limit(10)
                .offset(0)
                .build();

            Paging<Artist> artistPaging = getUsersTopArtistsRequest.execute();
            Artist[] artists = artistPaging.getItems();
            return Arrays.stream(artists)
                    .flatMap(artist -> Arrays.stream(artist.getGenres()))
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }
}

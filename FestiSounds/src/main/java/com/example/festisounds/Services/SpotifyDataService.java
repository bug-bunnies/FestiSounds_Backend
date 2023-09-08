package com.example.festisounds.Services;

import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.requests.data.personalization.simplified.GetUsersTopArtistsRequest;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.example.festisounds.Controllers.AuthController.spotifyApi;

@Service
public class SpotifyDataService implements ISpotifyDataService {

    @Override
    public Map<String, Long> userTopGenres() {
        Artist[] usersTopArtists = getUsersTopArtists();
        System.out.println(usersTopArtists);
        return Arrays.stream(usersTopArtists)
                .flatMap(artist -> Arrays.stream(artist.getGenres()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }

    @Override
    public Artist[] getUsersTopArtists() {
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

}

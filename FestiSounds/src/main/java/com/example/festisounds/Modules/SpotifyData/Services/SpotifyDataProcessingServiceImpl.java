package com.example.festisounds.Modules.SpotifyData.Services;

import com.example.festisounds.Core.Controllers.AuthController;
import com.example.festisounds.Modules.SpotifyData.DTOs.TopArtistsDTO;
import com.example.festisounds.Modules.SpotifyData.DTOs.TopItemsDTO;
import com.example.festisounds.Modules.SpotifyData.DTOs.TopTracksDTO;
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
import java.util.Arrays;
import java.util.HashMap;

import static com.example.festisounds.Core.Controllers.AuthController.spotifyApi;

@Service
public class SpotifyDataProcessingServiceImpl implements SpotifyDataProcessingService {

    private static final float ARTIST_WEIGHTING = 0.5F;
    private static final float SHORT_TERM_WEIGHTING = 0.1F;
    private static final float LONG_TERM_WEIGHTING = 0.1F;

    private final SpotifyDataService SpotifyDataService;

    public SpotifyDataProcessingServiceImpl(SpotifyDataService SpotifyDataService) {
        this.SpotifyDataService = SpotifyDataService;
    }
    @Override
    public HashMap<String, Double> rankUsersFavouriteGenres() throws IOException, ParseException, SpotifyWebApiException {
        TopItemsDTO usersTopArtistsAndTracks = SpotifyDataService.getUsersItems();
        HashMap<String, Double> genreRankingFromArtists = getGenreRankingFromArtists(usersTopArtistsAndTracks.topArtists());
        HashMap<String, Double> genreRankingFromTracks = getGenreRankingFromTracks(usersTopArtistsAndTracks.topTracks());
        return null;
    }
    private HashMap<String, Double> getGenreRankingFromArtists(TopArtistsDTO topArtistsDTO) {
        HashMap<String, Double> shortTermGenreRating = generateGenreRanking(topArtistsDTO.shortTermArtists());
        return null;
    }

    private HashMap<String, Double> generateGenreRanking(Artist[] artists) {
        HashMap<String, Double> genreRanking = new HashMap<>();
        double maxValue = (double) ((artists.length) * (artists.length + 1)) /2;
        for (int i = artists.length; i > 0; i--) {
            double finalI = i;
            Arrays.stream(artists[i-1].getGenres())
                    .map(genre -> genreRanking.merge(genre, finalI/(maxValue*100), Double::sum));
        }
        return genreRanking;
    }

    private HashMap<String, Double> getGenreRankingFromTracks(TopTracksDTO topTracksDTO) {
        return null;
    }


}

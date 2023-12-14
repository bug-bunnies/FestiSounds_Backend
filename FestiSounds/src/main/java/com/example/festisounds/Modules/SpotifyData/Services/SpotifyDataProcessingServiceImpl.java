package com.example.festisounds.Modules.SpotifyData.Services;

import com.example.festisounds.Modules.SpotifyData.DTOs.TopArtistsDTO;
import com.example.festisounds.Modules.SpotifyData.DTOs.TopItemsDTO;
import com.example.festisounds.Modules.SpotifyData.DTOs.TopTracksDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

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

    @Override
    public HashMap<String, Double> getGenreRankingFromArtists(TopArtistsDTO topArtistsDTO) {
        HashMap<String, Double> shortTermGenreRating = generateGenreRanking(topArtistsDTO.shortTermArtists());
        return null;
    }

    @Override
    public HashMap<String, Double> generateGenreRanking(Artist[] artists) {
        HashMap<String, Double> genreRanking = new HashMap<>();
        double maxValue = (double) ((artists.length) * (artists.length + 1)) /2;
        for (int i = 0; i < artists.length; i++) {
            double score = artists.length-i;
            for (String genre : artists[i].getGenres()) {
                genreRanking.merge(genre, score/(maxValue/100), Double::sum);
            }
        }
        return genreRanking;
    }

    @Override
    public HashMap<String, Double> getGenreRankingFromTracks(TopTracksDTO topTracksDTO) {
        return null;
    }


}

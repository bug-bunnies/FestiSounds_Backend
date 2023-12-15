package com.example.festisounds.Modules.SpotifyData.Services;

import com.example.festisounds.Modules.SpotifyData.DTOs.TopArtistsDTO;
import com.example.festisounds.Modules.SpotifyData.DTOs.TopItemsDTO;
import com.example.festisounds.Modules.SpotifyData.DTOs.TopTracksDTO;
import com.example.festisounds.Modules.SpotifyData.DTOs.WeightingsDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.util.HashMap;

@Service
public class SpotifyDataProcessingServiceImpl implements SpotifyDataProcessingService {

    private final float artistWeighting;
    private final float shortTermWeighting;
    private final float longTermWeighting;
    private final SpotifyDataService SpotifyDataService;

    @Autowired
    public SpotifyDataProcessingServiceImpl(SpotifyDataService SpotifyDataService) {
        this.SpotifyDataService = SpotifyDataService;
        this.artistWeighting = 0.6F;
        this.shortTermWeighting = 0.1F;
        this.longTermWeighting = 0.1F;
    }
    public SpotifyDataProcessingServiceImpl(@Autowired SpotifyDataService SpotifyDataService, WeightingsDTO weightings) {
        this.SpotifyDataService = SpotifyDataService;
        this.artistWeighting = weightings.longTermWeighting();
        this.shortTermWeighting = weightings.shortTermWeighting();
        this.longTermWeighting = weightings.longTermWeighting();
    }
    @Override
    public HashMap<String, Double> rankUsersFavouriteGenres() throws IOException, ParseException, SpotifyWebApiException {
        TopItemsDTO usersTopArtistsAndTracks = SpotifyDataService.getUsersItems();
        HashMap<String, Double> genreRankingFromArtists = getGenreRankingFromArtists(usersTopArtistsAndTracks.topArtists());
        //HashMap<String, Double> genreRankingFromTracks = getGenreRankingFromTracks(usersTopArtistsAndTracks.topTracks());
        return genreRankingFromArtists;
    }

    @Override
    public HashMap<String, Double> getGenreRankingFromArtists(TopArtistsDTO topArtistsDTO) {
        // Make these in parallel
        HashMap<String, Double> shortTermGenreRating = generateGenreRanking(topArtistsDTO.shortTermArtists());
        HashMap<String, Double> mediumTermGenreRating = generateGenreRanking(topArtistsDTO.mediumTermArtists());
        HashMap<String, Double> longTermGenreRating = generateGenreRanking(topArtistsDTO.longTermArtists());

        HashMap<String, Double> combinedGenreRating = new HashMap<>(shortTermGenreRating);
        mediumTermGenreRating.forEach(
                (key, value) -> combinedGenreRating.merge(key, value, (v1, v2) -> v1 + v2*(1-(shortTermWeighting+longTermWeighting))));
        longTermGenreRating.forEach(
                (key, value) -> combinedGenreRating.merge(key, value, (v1, v2) -> v1 + v2*longTermWeighting));

        return combinedGenreRating;
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

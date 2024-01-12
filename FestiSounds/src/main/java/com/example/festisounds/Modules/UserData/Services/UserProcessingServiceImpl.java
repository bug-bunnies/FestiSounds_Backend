package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopArtistsDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopItemsDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopTracksDTO;
import com.example.festisounds.Modules.UserData.DTOs.WeightingsDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.util.HashMap;

@Service
public class UserProcessingServiceImpl implements UserProcessingService {

    @Autowired
    CacheManager cacheManager;

    private final float artistWeighting;
    private final float shortTermWeighting;
    private final float longTermWeighting;
    private final UserRequestService UserRequestService;

    @Autowired
    public UserProcessingServiceImpl(UserRequestService UserRequestService) {
        this.UserRequestService = UserRequestService;
        this.artistWeighting = 0.6F;
        this.shortTermWeighting = 0.1F;
        this.longTermWeighting = 0.1F;
    }
    public UserProcessingServiceImpl(@Autowired UserRequestService UserRequestService, WeightingsDTO weightings) {
        this.UserRequestService = UserRequestService;
        this.artistWeighting = weightings.longTermWeighting();
        this.shortTermWeighting = weightings.shortTermWeighting();
        this.longTermWeighting = weightings.longTermWeighting();
    }

    @Cacheable(value="user-genre-data")
    @Override
    public HashMap<String, Double> rankUsersFavouriteGenres() throws IOException, ParseException, SpotifyWebApiException {
        TopItemsDTO usersTopArtistsAndTracks = UserRequestService.getUsersItems();
        HashMap<String, Double> genreRankingFromArtists = getGenreRankingFromArtists(usersTopArtistsAndTracks.topArtists());
        //HashMap<String, Double> genreRankingFromTracks = getGenreRankingFromTracks(usersTopArtistsAndTracks.topTracks());
        return genreRankingFromArtists;
    }

    @CacheEvict(value = "user-genre-data", allEntries = true)
    @Scheduled(fixedRateString = "${caching.spring.genreDataRefreshTTL}")
    public void emptyUserGenreDateCache() {
        // TODO: Make sure as soon as the user uses the app they have fresh data (when stuff is clicked).
    };



    @Override
    public HashMap<String, Double> getGenreRankingFromArtists(TopArtistsDTO topArtistsDTO) {
        // TODO: Make these in parallel
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

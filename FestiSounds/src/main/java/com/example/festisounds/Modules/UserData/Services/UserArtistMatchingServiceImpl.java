package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.Festival.DTO.FestivalDTO;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.SimpleKey;


import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserArtistMatchingServiceImpl implements UserArtistMatchingService {

    @Autowired
    CacheManager cacheManager;
    @Autowired
    private UserProcessingServiceImpl userProcessingService;
    @Autowired
    private FestivalService festivalService;
    @Autowired
    private UserCachingServiceImpl cachingService;
    @Value("${positionMap.location}")
    private String genrePositionMapFile;
    private final short colourNormaliser = 443;
    private final short colourWeighting = 1;
    private final short xAxisWeighting = 1;
    private final short yAxisWeighting = 3;


    @Override
    public LinkedHashMap<ArtistDTO, Double> getArtistRankingFromFestival(UUID festivalId)
            throws IOException, ParseException, SpotifyWebApiException {

        // Get user genre data
        HashMap<String, Double> genreData = userProcessingService.rankUsersFavouriteGenres();

        // Retrieve festival data
        FestivalDTO festival = festivalService.getFestivalById(festivalId);

        // Get genre map from cache
        HashMap<String, short[]> genrePositionMap = cachingService.buildAndCacheGenrePositionMap(genrePositionMapFile);


        return matchGenreDataToFestivalArtists(genreData, festival.artists(), genrePositionMap);
    }



    @Override
    public LinkedHashMap<ArtistDTO, Double> matchGenreDataToFestivalArtists(HashMap<String, Double> genreData,
                                                                            Set<ArtistDTO> artists,
                                                                            HashMap<String, short[]> genrePositions)
            throws IOException, ParseException, SpotifyWebApiException {

        HashMap<ArtistDTO, Double> artistScoresMap = new HashMap<>();

        for (ArtistDTO artist : artists) {
            Set<String> artistGenres = artist.genres();

            ArrayList<Double> genreScores = getGenreScore(genreData, artistGenres, genrePositions);
            Double artistScore = getArtistScore(genreScores);

            artistScoresMap.put(artist, artistScore);
        }

        return artistScoresMap
                .entrySet()
                .stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .collect(Collectors
                        .toMap(Map.Entry::getKey,
                                Map.Entry::getValue,
                                (a1, a2) -> a1, LinkedHashMap::new));
    }

    @Override
    public ArrayList<Double> getGenreScore(HashMap<String, Double> genreData, Set<String> artistGenres, HashMap<String, short[]> genrePositions) {
        ArrayList<Double> genreScores = new ArrayList<>();

        for (String artistGenre : artistGenres) {
            for (Map.Entry<String, Double> userGenre : genreData.entrySet()) {
                double distanceBetweenGenres = getDistanceBetweenGenres(artistGenre, userGenre.getKey(), genrePositions);

                        // score times (1 - distance) aka closeness

            }
        }


        return genreScores;
    }

    private double getDistanceBetweenGenres(String artistGenre, String userGenre, HashMap<String, short[]> genrePositions) {
        short[] artistGenrePosition = genrePositions.get(artistGenre);
        short[] userGenrePosition = genrePositions.get(userGenre);

        short[] maxValues = genrePositions.get("Max values");
        short xAxisNormaliser = maxValues[0];
        short yAxisNormaliser = maxValues[1];

        double xDistanceSquared = (1/Math.pow(xAxisNormaliser, 2))*xAxisWeighting*Math.pow((artistGenrePosition[0] - userGenrePosition[0]), 2);
        double yDistanceSquared = (1/Math.pow(yAxisNormaliser, 2))*yAxisWeighting*Math.pow((artistGenrePosition[1] - userGenrePosition[1]), 2);
        double colourDistanceSquared = calculateColourDistanceSquared(artistGenrePosition, userGenrePosition);

        return Math.sqrt(xDistanceSquared + yDistanceSquared + colourDistanceSquared);
    }

    private double calculateColourDistanceSquared(short[] artistGenrePosition, short[] userGenrePosition) {
        double rawDistance = Math.pow((artistGenrePosition[2] - userGenrePosition[2]), 2)
                + Math.pow((artistGenrePosition[3] - userGenrePosition[3]), 2)
                + Math.pow((artistGenrePosition[4] - userGenrePosition[4]), 2);
        return rawDistance*(1/Math.pow(colourNormaliser, 2))*colourWeighting;
    }


    @Override
    public double getArtistScore(ArrayList<Double> genreScores) {
        double artistScore = 0;
        for (Double score : genreScores) {
            double remainder = 100 - artistScore;
            artistScore += (remainder/100)*score;
        }
        return artistScore;
    }
}

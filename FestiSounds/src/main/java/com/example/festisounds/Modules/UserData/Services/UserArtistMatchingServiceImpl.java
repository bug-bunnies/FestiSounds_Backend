package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserArtistMatchingServiceImpl implements UserArtistMatchingService {

    private final short colourNormaliser = 443;
    private final short colourWeighting = 1;
    private final short xAxisWeighting = 1;
    private final short yAxisWeighting = 3;
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
    public LinkedHashMap<ArtistResponseDTO, Double> getArtistRankingFromFestival(UUID festivalId)
            throws IOException, ParseException, SpotifyWebApiException {
        // Get user genre data
        HashMap<String, Double> genreData = userProcessingService.rankUsersFavouriteGenres();
        // Get festival artists
        FestivalResponseDTO festival = festivalService.getFestivalById(festivalId);
        // Get genre map from cache
        HashMap<String, short[]> genrePositionMap = cachingService.buildAndCacheGenrePositionMap(genrePositionMapFile);

        return matchGenreDataToFestivalArtists(genreData, festival.artists(), genrePositionMap);
    }

    @Override
    public LinkedHashMap<ArtistResponseDTO, Double> matchGenreDataToFestivalArtists(HashMap<String, Double> genreData,
                                                                                    Set<ArtistResponseDTO> artists,
                                                                                    HashMap<String, short[]> genrePositionMap)
            throws IOException, ParseException, SpotifyWebApiException {

        HashMap<ArtistResponseDTO, Double> artistScoresMap = new HashMap<>();
        System.out.println("MATCHING VALUES START");
        for (ArtistResponseDTO artist : artists) {

            Set<String> artistGenres = artist.genres();
            if (artistGenres.isEmpty()) continue;

            Double artistScore = getArtistScore(genreData, artistGenres, genrePositionMap);

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
    public double getArtistScore(HashMap<String, Double> genreData, Set<String> artistGenres, HashMap<String, short[]> genrePositionMap) {
        ArrayList<Double> genreScores = new ArrayList<>();

        OUTER:
        for (String artistGenre : artistGenres) {
            double genresRatingSum = 0;
            int missingUserGenres = 0;
            for (Map.Entry<String, Double> userGenre : genreData.entrySet()) {

                short[] artistGenrePosition = genrePositionMap.get(artistGenre.trim());
                if (artistGenrePosition == null) {
                    continue OUTER;
                }

                short[] userGenrePosition = genrePositionMap.get(userGenre.getKey().trim());
                if (userGenrePosition == null) {
                    missingUserGenres++;
                    continue;
                }

                double distanceBetweenGenres = getDistanceBetweenGenres(artistGenrePosition, userGenrePosition, genrePositionMap);
                double distanceRating = userGenre.getValue() * (100 - distanceBetweenGenres);
                genresRatingSum += distanceRating;
            }
            genreScores.add(genresRatingSum / (100 * (genreData.size() - missingUserGenres)));
        }

        return genreScores
                .stream()
                .mapToDouble(x -> x)
                .average()
                .orElseThrow();
    }

    public double getDistanceBetweenGenres(short[] artistGenrePosition, short[] userGenrePosition, HashMap<String, short[]> genrePositionMap) {
        short[] maxValues = genrePositionMap.get("Max values");
        short xAxisNormaliser = maxValues[0];
        short yAxisNormaliser = maxValues[1];

        double xDistanceSquared = (1 / Math.pow(xAxisNormaliser, 2)) * xAxisWeighting * Math.pow((artistGenrePosition[0] - userGenrePosition[0]), 2);
        double yDistanceSquared = (1 / Math.pow(yAxisNormaliser, 2)) * yAxisWeighting * Math.pow((artistGenrePosition[1] - userGenrePosition[1]), 2);
        double colourDistanceSquared = calculateColourDistanceSquared(artistGenrePosition, userGenrePosition);

        double rawDistance = Math.sqrt(xDistanceSquared + yDistanceSquared + colourDistanceSquared);
        return (100 / Math.sqrt(xAxisWeighting + yAxisWeighting + colourWeighting)) * rawDistance;
    }

    public double calculateColourDistanceSquared(short[] artistGenrePosition, short[] userGenrePosition) {
        double rawDistance = Math.pow((artistGenrePosition[2] - userGenrePosition[2]), 2)
                + Math.pow((artistGenrePosition[3] - userGenrePosition[3]), 2)
                + Math.pow((artistGenrePosition[4] - userGenrePosition[4]), 2);
        return rawDistance * (1 / Math.pow(colourNormaliser, 2)) * colourWeighting;
    }
}

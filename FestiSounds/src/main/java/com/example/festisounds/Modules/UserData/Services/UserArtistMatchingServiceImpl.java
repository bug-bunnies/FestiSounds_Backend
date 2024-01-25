package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

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
    public LinkedHashMap<ArtistResponseDTO, Double> getArtistRankingFromFestival(UUID festivalId)
            throws IOException, ParseException, SpotifyWebApiException {

        // Get user genre data
        HashMap<String, Double> genreData = userProcessingService.rankUsersFavouriteGenres();
        System.out.println("getting user genre data");
        // Get festival artists
        FestivalResponseDTO festival = festivalService.getFestivalById(festivalId);
        System.out.println("getting festival dto");
        // Get genre map from cache
        HashMap<String, short[]> genrePositionMap = cachingService.buildAndCacheGenrePositionMap(genrePositionMapFile);
        System.out.println("getting x y z map");

        return matchGenreDataToFestivalArtists(genreData, festival.artists(), genrePositionMap);
    }

    @Override
    public LinkedHashMap<ArtistResponseDTO, Double> matchGenreDataToFestivalArtists(HashMap<String, Double> genreData,
                                                                            Set<ArtistResponseDTO> artists,
                                                                            HashMap<String, short[]> genrePositions)
            throws IOException, ParseException, SpotifyWebApiException {

        HashMap<ArtistResponseDTO, Double> artistScoresMap = new HashMap<>();
        System.out.println("MATCHING VALUES START");
        for (ArtistResponseDTO artist : artists) {
        System.out.println("MATCHING VALUES artist " + artist.artistName());

            Set<String> artistGenres = artist.genres();
            if (artistGenres.isEmpty()) continue;
        System.out.println("MATCHING VALUES genres " + artistGenres);

            Double artistScore = getArtistScore(genreData, artistGenres, genrePositions);
        System.out.println("MATCHING VALUES Score " + artistScore);

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
    public double getArtistScore(HashMap<String, Double> genreData, Set<String> artistGenres, HashMap<String, short[]> genrePositions) {
        ArrayList<Double> genreScores = new ArrayList<>();

        for (String artistGenre : artistGenres) {
            double genresRatingSum = 0;
            for (Map.Entry<String, Double> userGenre : genreData.entrySet()) {
                double distanceBetweenGenres = getDistanceBetweenGenres(artistGenre, userGenre.getKey(), genrePositions);

                double distanceRating = userGenre.getValue()*(100-distanceBetweenGenres);
                genresRatingSum += distanceRating;
            }
            genreScores.add(genresRatingSum/(100*genreData.size()));
        }

        return genreScores
                .stream()
                .mapToDouble(x-> x)
                .average()
                .orElseThrow();
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

        double rawDistance = Math.sqrt(xDistanceSquared + yDistanceSquared + colourDistanceSquared);
        return Math.sqrt(xAxisWeighting + yAxisWeighting + colourWeighting)*(100/rawDistance);
    }

    private double calculateColourDistanceSquared(short[] artistGenrePosition, short[] userGenrePosition) {
        double rawDistance = Math.pow((artistGenrePosition[2] - userGenrePosition[2]), 2)
                + Math.pow((artistGenrePosition[3] - userGenrePosition[3]), 2)
                + Math.pow((artistGenrePosition[4] - userGenrePosition[4]), 2);
        return rawDistance*(1/Math.pow(colourNormaliser, 2))*colourWeighting;
    }
}

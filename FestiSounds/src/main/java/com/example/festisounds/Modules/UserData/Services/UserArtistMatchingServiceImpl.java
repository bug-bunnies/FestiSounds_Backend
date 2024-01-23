package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public LinkedHashMap<ArtistResponseDTO, Double> getArtistRankingFromFestival(UUID festivalId)
            throws IOException, ParseException, SpotifyWebApiException {

        HashMap<String, Double> genreData = userProcessingService.rankUsersFavouriteGenres();
        FestivalResponseDTO festival = festivalService.getFestivalById(festivalId);

        return matchGenreDataToFestivalArtists(genreData, festival.artists());
    }

    @Override
    public LinkedHashMap<ArtistResponseDTO, Double> matchGenreDataToFestivalArtists(HashMap<String, Double> genreData, Set<ArtistResponseDTO> artists)
            throws IOException, ParseException, SpotifyWebApiException {

        HashMap<ArtistResponseDTO, Double> artistScoresMap = new HashMap<>();

        for (ArtistResponseDTO artist : artists) {
            Set<String> artistGenres = artist.genres();

            ArrayList<Double> genreScores = getGenreScore(genreData, artistGenres);
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
    public ArrayList<Double> getGenreScore(HashMap<String, Double> genreData, Set<String> artistGenres) {
        ArrayList<Double> genreScores = new ArrayList<>();
        for (String artistGenre : artistGenres) {
            if (genreData.containsKey(artistGenre.trim())) {
                genreScores.add(genreData.get(artistGenre.trim()));
            }
        }
        genreScores.sort(Collections.reverseOrder());
        return genreScores;
    }

    @Override
    public double getArtistScore(ArrayList<Double> genreScores) {
        double artistScore = 0;
        for (Double score : genreScores) {
            double remainder = 100 - artistScore;
            artistScore += (remainder / 100) * score;
        }
        return artistScore;
    }
}

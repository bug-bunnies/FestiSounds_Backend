package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.*;

public interface UserArtistMatchingService {
    LinkedHashMap<ArtistResponseDTO, Double> getArtistRankingFromFestival(UUID festivalId)
            throws IOException, ParseException, SpotifyWebApiException;
    LinkedHashMap<ArtistResponseDTO, Double> matchGenreDataToFestivalArtists(HashMap<String, Double> genreData, Set<ArtistResponseDTO> artists, HashMap<String, short[]> genrePositionMap)
            throws IOException, ParseException, SpotifyWebApiException;

    double getArtistScore(HashMap<String, Double> genreData, Set<String> artistGenres, HashMap<String, short[]> genrePositionMap);
}

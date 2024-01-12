package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopArtistsDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopTracksDTO;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public interface UserArtistMatchingService {
    HashMap<ArtistDTO, Double> getArtistRankingFromFestival(UUID festivalId)
            throws IOException, ParseException, SpotifyWebApiException;
    HashMap<ArtistDTO, Double> matchGenreDataToFestivalArtists(HashMap<String, Double> genreData, Set<ArtistDTO> artists)
            throws IOException, ParseException, SpotifyWebApiException;
}

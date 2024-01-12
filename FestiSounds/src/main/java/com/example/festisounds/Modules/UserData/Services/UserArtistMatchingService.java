package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopArtistsDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopTracksDTO;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.util.HashMap;

public interface UserArtistMatchingService {
    HashMap<ArtistDTO, Double> getArtistRankingFromFestival(String festivalId)
            throws IOException, ParseException, SpotifyWebApiException;
}

package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.UserData.DTOs.SpotifyUserDataDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopArtistsDTO;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;

public interface UserRequestService {

    TopArtistsDTO getUsersTopArtists() throws IOException, ParseException, SpotifyWebApiException;
  
    Artist[] getUsersArtistsForTimeframe(String timeframe) throws IOException, ParseException, SpotifyWebApiException;

    SpotifyUserDataDTO getUserSpotifyInfo() throws IOException, ParseException, SpotifyWebApiException;
}

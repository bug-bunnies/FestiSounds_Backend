package com.example.festisounds.Modules.SpotifyData.Services;

import com.example.festisounds.Modules.SpotifyData.DTOs.TopArtistTimeframeDTO;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.util.Map;

public interface SpotifyDataService {

//    Map<String, Long> userTopGenres() throws IOException, ParseException, SpotifyWebApiException;

//    Artist[] getUsersTopArtists() throws IOException, ParseException, SpotifyWebApiException;
    TopArtistTimeframeDTO getUsersArtists() throws IOException, ParseException, SpotifyWebApiException;
    Artist[] getUsersArtistsForTimeframe(String timeframe) throws IOException, ParseException, SpotifyWebApiException;

}

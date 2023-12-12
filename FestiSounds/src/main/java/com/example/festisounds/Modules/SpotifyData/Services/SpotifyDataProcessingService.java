package com.example.festisounds.Modules.SpotifyData.Services;

import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;

public interface SpotifyDataProcessingService {
    HashMap<String, Double> rankUsersFavouriteGenres() throws IOException, ParseException, SpotifyWebApiException, org.apache.hc.core5.http.ParseException;
}

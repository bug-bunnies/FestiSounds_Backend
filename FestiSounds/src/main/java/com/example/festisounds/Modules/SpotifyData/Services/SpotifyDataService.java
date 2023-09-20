package com.example.festisounds.Modules.SpotifyData.Services;

import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.util.Map;

public interface SpotifyDataService {

    Map<String, Long> userTopGenres() throws IOException, ParseException, SpotifyWebApiException;

    Artist[] getUsersTopArtists() throws IOException, ParseException, SpotifyWebApiException;
}

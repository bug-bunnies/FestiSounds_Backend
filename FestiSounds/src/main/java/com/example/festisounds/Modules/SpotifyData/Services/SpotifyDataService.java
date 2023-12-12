package com.example.festisounds.Modules.SpotifyData.Services;

import com.example.festisounds.Modules.SpotifyData.DTOs.TopItemsDTO;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

import java.io.IOException;

public interface SpotifyDataService {

    TopItemsDTO getUsersItems() throws IOException, ParseException, SpotifyWebApiException;
    Artist[] getUsersArtistsForTimeframe(String timeframe) throws IOException, ParseException, SpotifyWebApiException;
    Track[] getUsersTracksForTimeframe(String timeframe) throws IOException, ParseException, SpotifyWebApiException;

}

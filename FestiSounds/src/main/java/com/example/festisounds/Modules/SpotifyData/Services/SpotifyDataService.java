package com.example.festisounds.Modules.SpotifyData.Services;

import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.Map;

public interface SpotifyDataService {

    Map<String, Long> userTopGenres();

    Artist[] getUsersTopArtists();
}

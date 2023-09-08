package com.example.festisounds.Services;

import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.Map;

public interface ISpotifyDataService {

    Map<String, Long> userTopGenres();

    Artist[] getUsersTopArtists();
}

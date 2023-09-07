package com.example.festisounds.Service;

import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.Map;

public interface ISpotifyDataService {

    Map<String, Long> userTopGenres() throws Exception;

    Artist[] getUsersTopArtists();
}

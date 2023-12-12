package com.example.festisounds.Modules.SpotifyData.DTOs;

import se.michaelthelin.spotify.model_objects.specification.Artist;

public record TopArtistsDTO(Artist[] shortTermArtists,
                            Artist[] mediumTermArtists,
                            Artist[] longTermArtists) {
}

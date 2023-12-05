package com.example.festisounds.Modules.SpotifyData.DTOs;

import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.model_objects.specification.Track;

public record TopItemsTimeframeDTO(Artist[] shortTermArtists,
                                   Artist[] mediumTermArtists,
                                   Artist[] longTermArtists,
                                   Track[] shortTermTracks,
                                   Track[] mediumTermTracks,
                                   Track[] longTermTracks) {
}

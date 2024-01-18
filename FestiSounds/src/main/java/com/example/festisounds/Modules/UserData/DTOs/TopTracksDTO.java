package com.example.festisounds.Modules.UserData.DTOs;

import se.michaelthelin.spotify.model_objects.specification.Track;

public record TopTracksDTO(Track[] shortTermArtists,
                           Track[] mediumTermArtists,
                           Track[] longTermArtists) {
}

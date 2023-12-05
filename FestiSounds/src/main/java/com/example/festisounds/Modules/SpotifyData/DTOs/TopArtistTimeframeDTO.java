package com.example.festisounds.Modules.SpotifyData.DTOs;

import se.michaelthelin.spotify.model_objects.specification.Artist;

public record TopArtistTimeframeDTO(Artist[] shortTerm, Artist[] mediumTerm, Artist[] longTerm) {
}

package com.example.festisounds.Modules.FestivalArtists.DTO;

import java.util.UUID;

public record ArtistDTO(UUID id,
                        String spotifyId,
                        String name) {
}

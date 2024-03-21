package com.example.festisounds.Modules.FestivalArtists.DTO;

import com.example.festisounds.Modules.Festival.Entities.Festival;

import java.util.Set;
import java.util.UUID;

public record ArtistRequestDTO(String spotifyId,
                               String artistName,
                               Set<String> genres) {
}

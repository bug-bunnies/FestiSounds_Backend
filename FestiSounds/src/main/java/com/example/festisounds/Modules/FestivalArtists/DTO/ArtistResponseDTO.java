package com.example.festisounds.Modules.FestivalArtists.DTO;

import java.util.Set;
import java.util.UUID;

public record ArtistResponseDTO(UUID id,
                                String spotifyId,
                                String artistName,
                                Set<String> genres,
                                Set<UUID> festivalId) {
}

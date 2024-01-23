package com.example.festisounds.Modules.Festival.DTO;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record FestivalResponseDTO(
        UUID id,
        String name,
        Date startDate,
        Date endDate,
        String details,
        Set<ArtistResponseDTO> artists,
        String city,
        String country,
        String organizer,
        String website
        ) {
}

package com.example.festisounds.Modules.Festival.DTO;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

public record FestivalDTO(
        UUID id,
        String name,
        Date startDate,
        Date endDate,
        String details,
        Set<ArtistDTO> artists,
        String city,
        String country,
        String organizer,
        String website
        ) {
}

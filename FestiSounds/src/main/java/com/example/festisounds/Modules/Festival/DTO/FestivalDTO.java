package com.example.festisounds.Modules.Festival.DTO;

import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;

import java.util.Date;
import java.util.Set;

public record FestivalDTO(
        String name,
        Date startDate,
        Date endDate,
        String details,
        Set<FestivalArtist> artists,
        String city,
        String country,
        String organizer,
        String website
        ) {
}

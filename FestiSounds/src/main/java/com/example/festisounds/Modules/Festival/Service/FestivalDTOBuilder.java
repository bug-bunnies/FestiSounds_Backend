package com.example.festisounds.Modules.Festival.Service;

import com.example.festisounds.Modules.Festival.DTO.FestivalDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;

public class FestivalDTOBuilder {
    public static FestivalDTO festivalDataBuilder(Festival festival) {
        return new FestivalDTO(festival.getId(),
                festival.getName(),
                festival.getStartDate(),
                festival.getEndDate(),
                festival.getDetails(),
                festival.getArtists(),
                festival.getCity(),
                festival.getCountry(),
                festival.getOrganizer(),
                festival.getWebsite());
    }
}

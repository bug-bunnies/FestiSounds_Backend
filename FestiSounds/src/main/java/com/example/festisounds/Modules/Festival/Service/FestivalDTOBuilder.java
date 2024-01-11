package com.example.festisounds.Modules.Festival.Service;

import com.example.festisounds.Modules.Festival.DTO.FestivalDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;

import java.util.stream.Collectors;


public class FestivalDTOBuilder {
    public static FestivalDTO festivalDataBuilder(Festival festival) {
        return new FestivalDTO(festival.getId(),
                festival.getName(),
                festival.getStartDate(),
                festival.getEndDate(),
                festival.getDetails(),
                festival.getArtists()
                        .stream()
                        .map(FestivalDTOBuilder::artistDataBuilder)
                        .collect(Collectors.toSet()),
                festival.getCity(),
                festival.getCountry(),
                festival.getOrganizer(),
                festival.getWebsite());
    }

    public static ArtistDTO artistDataBuilder(FestivalArtist artist) {
        return new ArtistDTO(artist.getId(),
                artist.getSpotifyId(),
                artist.getArtistName(),
                artist.getGenres(),
                artist.getFestivals()
                        .stream()
                        .map(f -> f.getName())
                        .collect(Collectors.toSet()));
    }
}

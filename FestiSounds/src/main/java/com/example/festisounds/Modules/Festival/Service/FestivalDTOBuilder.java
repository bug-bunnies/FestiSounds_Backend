package com.example.festisounds.Modules.Festival.Service;

import com.example.festisounds.Modules.Festival.DTO.FestivalRequestDTO;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;

import java.util.Set;
import java.util.stream.Collectors;


public class FestivalDTOBuilder {
    public static FestivalResponseDTO festivalDTOBuilder(Festival festival) {
        return new FestivalResponseDTO(
                festival.getId(),
                festival.getName(),
                festival.getStartDate(),
                festival.getEndDate(),
                festival.getDetails(),
                festival.getArtists()
                        .stream()
                        .map(FestivalDTOBuilder::artistDTOBuilder)
                        .collect(Collectors.toSet()),
                festival.getCity(),
                festival.getCountry(),
                festival.getOrganizer(),
                festival.getWebsite());
    }

    public static FestivalResponseDTO festivalDTOBuilder(Festival festival, Set<ArtistResponseDTO> artists) {
        return new FestivalResponseDTO(
                festival.getId(),
                festival.getName(),
                festival.getStartDate(),
                festival.getEndDate(),
                festival.getDetails(),
                artists,
                festival.getCity(),
                festival.getCountry(),
                festival.getOrganizer(),
                festival.getWebsite());
    }

    public static ArtistResponseDTO artistDTOBuilder(FestivalArtist artist) {
        return new ArtistResponseDTO(
                artist.getId(),
                artist.getSpotifyId(),
                artist.getArtistName(),
                artist.getGenres(),
                artist.getFestivals()
                        .stream()
                        .map(Festival::getId)
                        .collect(Collectors.toSet()));
    }

    public static Festival festivalEntityBuilder(FestivalRequestDTO festival) {
        return Festival.builder()
                .name(festival.name())
                .startDate(festival.startDate())
                .endDate(festival.endDate())
                .details(festival.details())
                .city(festival.city())
                .country(festival.country())
                .organizer(festival.organizer())
                .website(festival.website())
                .build();
    }

    private static FestivalArtist artistEntityBuilder(String spotifyId, String artistName) {
        return FestivalArtist.builder()
                .spotifyId(spotifyId)
                .artistName(artistName)
                .build();
    }
}

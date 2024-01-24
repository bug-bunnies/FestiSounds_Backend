package com.example.festisounds.Modules.Festival.Services;

import com.example.festisounds.Modules.Festival.DTO.FestivalRequestDTO;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.Repositories.FestivalArtistRepository;
import com.example.festisounds.Modules.FestivalArtists.Service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FestivalServiceTest {

    @Mock
    FestivalRepo festivalRepo;

    @Mock
    FestivalArtistRepository artistRepository;

    @Mock
    ArtistService artistService;

    @InjectMocks
    FestivalService festivalService;

    FestivalRequestDTO festivalRequestData;

    Festival festival1;
    Festival festival2;
    List<Festival> festivals;

    @BeforeEach
    void setup() {
        festivalRequestData = FestivalRequestDTO.builder()
                .name("TestFestivalName")
                .startDate(new Date())
                .endDate(new Date())
                .details("TestDetails")
                .artists(Set.of("TestArtist1"))
                .city("TestCity")
                .country("TestCountry")
                .organizer("TestOrganizer")
                .website("www.testWebsite.com")
                .build();

        festival1 = new Festival();
        festival1.setName("TestFestival1");
        festival2 = new Festival();
        festival2.setName("TestFestival2");
        festivals = Arrays.asList(festival1, festival2);
    }


    @Test
    @Disabled
    @DisplayName("Create festival with artist")
    public void testCreateFestival_whenGivenCorrectDetailsWithArtist_ReturnsCorrectResponse() {
    }

    @Test
    @DisplayName("Get all festivals")
    public void testGetAllFestivals_whenTwoFestivalsExists_returnsAnArrayOfFestivals() {
        // Arrange
        when(festivalRepo.findAll()).thenReturn(festivals);

        // Act
        FestivalResponseDTO[] festivalsFromService = festivalService.getAllFestivals();

        // Assert
        assertEquals(2, festivalsFromService.length, "festivalsFromService should have a size of 2, but instead was: " + festivalsFromService.length);
        assertEquals("TestFestival1", festivalsFromService[0].name(), "The first festival should have the name TestFestival1 but instead was: " + festivalsFromService[0].name());
        verify(festivalRepo).findAll();
    }

    @Test
    @DisplayName("Get festival by ID")
    public void testGetFestivalById_whenGivenValidId_returnsFestivalResponse() {
        // Arrange
        UUID id = UUID.randomUUID();
        festival1.setId(id);

        when(festivalRepo.findById(id)).thenReturn(Optional.of(festival1));

        // Act
        FestivalResponseDTO result = festivalService.getFestivalById(id);

        // Assert
        assertEquals(id, result.id());
        verify(festivalRepo).findById(id);
    }
}

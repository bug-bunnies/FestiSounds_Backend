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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
    }


    @Test
    @Disabled
    @DisplayName("Create festival with artist")
    public void testCreateFestival_whenGivenCorrectDetailsWithArtist_ReturnsCorrectResponse() {

    }

    @Test
    @DisplayName("Returns all festivals")
    public void testGetAllFestivals_whenTwoFestivalsExists_returnsAnArrayOfFestivals() {
        // Arrange
        Festival festival1 = new Festival();
        festival1.setName("TestFestival");
        Festival festival2 = new Festival();
        List<Festival> festivals = Arrays.asList(festival1, festival2);

        when(festivalRepo.findAll()).thenReturn(festivals);

        // Act
        FestivalResponseDTO[] result = festivalService.getAllFestivals();

        // Assert
        assertEquals(2, result.length, "result should have a size of 2, but instead was: " + result.length);
        assertEquals("TestFestival", result[0].name(), "The first festival should have the name TestFestival but instead was: " + result[0].name());
        verify(festivalRepo).findAll();
    }
}

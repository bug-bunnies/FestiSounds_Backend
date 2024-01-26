package com.example.festisounds.Modules.Festival.Services;

import com.example.festisounds.Core.Exceptions.Festival.FestivalNotFoundException;
import com.example.festisounds.Modules.Festival.DTO.FestivalRequestDTO;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Service.ArtistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.example.festisounds.Modules.Festival.Service.FestivalDTOBuilder.festivalEntityBuilder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FestivalServiceTest {

    @Mock
    FestivalRepo festivalRepo;

    @Mock
    ArtistService artistService;

    @InjectMocks
    FestivalService festivalService;

    FestivalRequestDTO festivalRequestData;
    List<Festival> emptyFestivalEntityList;
    Festival storedFestival;

    Festival festival1;
    String nameSearchQuery;
    Festival festival2;
    List<Festival> festivals;
    ArtistResponseDTO artistResponseDTO;

    UUID festivalId1;

    @BeforeEach
    void setup() {
        festivalRequestData = FestivalRequestDTO.builder()
                .name("TestFestival1")
                .startDate(new Date())
                .endDate(new Date())
                .details("TestDetails")
                .artists(Set.of())
                .city("TestCity")
                .country("TestCountry")
                .organizer("TestOrganizer")
                .website("www.testWebsite.com")
                .build();

        storedFestival = festivalEntityBuilder(festivalRequestData);

        emptyFestivalEntityList = new ArrayList<>();

        festivalId1 = UUID.randomUUID();
        festival1 = new Festival();
        festival1.setId(festivalId1);
        festival1.setName("TestFestival1");
        nameSearchQuery = festival1.getName();

        festival2 = new Festival();
        festival2.setName("TestFestival2");

        festivals = Arrays.asList(festival1, festival2);

        artistResponseDTO = new ArtistResponseDTO(UUID.randomUUID(), "ArtistSpotifyId", "TestArtist1", Set.of("Genre1", "Genre2"), Set.of(festivalId1));
    }


    @Test
    @DisplayName("Create festival without artist")
    public void testCreateFestival_whenGivenCorrectDetailsWithoutArtist_ReturnsCorrectResponse() {
//        Arrange
        when(festivalRepo.save(any(Festival.class))).thenReturn(storedFestival);

//        Act
        FestivalResponseDTO result = festivalService.createFestival(festivalRequestData);

//        Assert
        verify(festivalRepo).save(any(Festival.class));
        assertEquals(storedFestival.getName(), result.name());
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
    @DisplayName("Get all festivals throws FestivalNotFoundException.")
    public void testGetAllFestivals_whenFestivalsDontExist_throwsException() {
//        Arrange
        when(festivalRepo.findAll()).thenReturn(emptyFestivalEntityList);

//        Act
        Exception exception = assertThrows(FestivalNotFoundException.class,
                () -> festivalService.getAllFestivals());

//        Assert
        assertTrue(exception.getMessage().contains("No festivals found."));
    }

    @Test
    @DisplayName("Get festival by ID")
    public void testGetFestivalById_whenGivenValidId_returnsFestivalResponse() {
        // Arrange
        when(festivalRepo.findById(festivalId1)).thenReturn(Optional.of(festival1));

        // Act
        FestivalResponseDTO result = festivalService.getFestivalById(festivalId1);

        // Assert
        assertEquals(festivalId1, result.id());
        verify(festivalRepo).findById(festivalId1);
    }

    @Test
    @DisplayName("Get festival by id throws FestivalNotFoundException.")
    public void testGetFestivalById_whenFestivalDoesntExist_throwsException() {
//        Arrange
        when(festivalRepo.findById(festivalId1)).thenReturn(Optional.empty());

//        Act
        Exception exception = assertThrows(FestivalNotFoundException.class,
                () -> festivalService.getFestivalById(festivalId1));

//        Assert
        assertTrue(exception.getMessage().contains("Could not find a festival by the id: " + festivalId1));
    }


    @Test
    @DisplayName("Get festival by name")
    public void testGetFestivalByName_whenGivenValidName_returnsFestivalResponse() {
        // Arrange
        String searchQuery = festival1.getName();
        when(festivalRepo.findByNameContainingIgnoreCase(searchQuery)).thenReturn(List.of(festival1));

        // Act
        FestivalResponseDTO[] result = festivalService.getFestivalsByName(searchQuery);

        // Assert
        assertEquals(1, result.length, "result should have a size of 1, but instead was: " + result.length);
        assertEquals(searchQuery, result[0].name(), "The first festival should have the name TestFestival1 but instead was: " + result[0].name());
        verify(festivalRepo).findByNameContainingIgnoreCase(searchQuery);
    }

    @Test
    @DisplayName("Get festival by name throws FestivalNotFoundException.")
    public void testGetFestivalByName_whenFestivalDoesntExist_throwsException() {
//        Arrange
        when(festivalRepo.findByNameContainingIgnoreCase(nameSearchQuery)).thenReturn(emptyFestivalEntityList);

//        Act
        Exception exception = assertThrows(FestivalNotFoundException.class,
                () -> festivalService.getFestivalsByName(nameSearchQuery));

//        Assert
        assertTrue(exception.getMessage().contains("Could not find a festival by the name: " + nameSearchQuery));
    }

    @Test
    @DisplayName("Delete festival")
    public void testDeleteFestival_whenValidUUIDProvided_deletesFestival() {
        // Arrange
        when(festivalRepo.findById(festivalId1)).thenReturn(Optional.of(festival1));

        // Act
        festivalService.deleteFestival(festivalId1);

        // Assert
        verify(festivalRepo).deleteById(festivalId1);
        verify(festivalRepo).findById(festivalId1);
    }
}

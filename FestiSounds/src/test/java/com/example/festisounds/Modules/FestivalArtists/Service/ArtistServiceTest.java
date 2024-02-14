package com.example.festisounds.Modules.FestivalArtists.Service;

import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistRequestDTO;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;
import com.example.festisounds.Modules.FestivalArtists.Repositories.FestivalArtistRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class ArtistServiceTest {

    @InjectMocks
    ArtistService artistService;

    @Mock
    FestivalArtistRepository artistRepository;

    private Festival festival1;
    private ArtistRequestDTO artistRequest;
    private ArtistResponseDTO artistResponse1;
    private FestivalArtist artistEntity1;
    private UUID artistEntity1ID;
    private String artistName1;
    private String spotifyId1;
    private Set<String> genres;

    @BeforeEach
    void setup() {
        artistName1 = "artist1";
        spotifyId1 = "spotifyId1";
        genres = Set.of("test1");
        artistEntity1ID = UUID.randomUUID();

        festival1 = new Festival();
        festival1.setName("festival1");
        festival1.setId(UUID.randomUUID());
        festival1.setCity("festivalCity");
        festival1.setDetails("test");
        festival1.setCountry("festival country");
        festival1.setEndDate(new Date());
        festival1.setStartDate(new Date());
        festival1.setWebsite("website");
        festival1.setOrganizer("organizer");

        artistRequest = new ArtistRequestDTO(spotifyId1,
                artistName1,
                genres);

        artistEntity1 = new FestivalArtist(artistEntity1ID,
                artistName1,
                spotifyId1,
                Set.of(festival1),
                genres);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createArtist_whenGivenArtistNewData_createArtist() {
        when(artistRepository.save(any(FestivalArtist.class))).thenReturn(artistEntity1);

        ArtistResponseDTO createdArtist = artistService.createArtist(festival1, artistRequest);

        verify(artistRepository).save(any(FestivalArtist.class));
//        Assertions.assertEquals(artistEntity1.getArtistName(), createdArtist.artistName(),
//                () -> "Artists name does not match");
    }

    @Test
    void createOrAddArtistRouter_whenArtistDoesNotExist_createNewArtist() {

    }

    @Test
    void getArtist_whenGivenExistingArtistId_returnsArtist() {
        // Arrange
        when(artistRepository.findById(artistEntity1ID)).thenReturn(Optional.of(artistEntity1));

        // Act
        ArtistResponseDTO result = artistService.getArtist(artistEntity1ID);

        // Assert
        assertEquals(artistEntity1, result.id());
        verify(artistRepository).findById(artistEntity1ID);
    }


}

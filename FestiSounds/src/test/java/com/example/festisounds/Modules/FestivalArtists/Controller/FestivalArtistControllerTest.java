package com.example.festisounds.Modules.FestivalArtists.Controller;

import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Service.ArtistService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = ArtistController.class)
public class FestivalArtistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ArtistService service;

    UUID festivalUUID = UUID.randomUUID();
    ArtistResponseDTO artistResponseDTO;

    @BeforeEach
    void setup() {
        artistResponseDTO = new ArtistResponseDTO(UUID.randomUUID(),
                "ArtistSpotifyId",
                "ArtistName",
                Set.of("Genre1", "Genre2"),
                Set.of(festivalUUID));
    }

    @Test
    @DisplayName("Find Artist by name in Spotify")
    void testGetAllArtist_whenGivenASearchParam_returnsArrayOfArtists() {

    }

    @Test
    @DisplayName("Get Artist by ID returns right artist")
    void testFindArtist_whenGivenExistingId_returnsRightArtist() throws Exception {
        UUID id = artistResponseDTO.id();
        when(service.getArtist(id)).thenReturn(artistResponseDTO);
        TypeReference<ArtistResponseDTO> typeRef = new TypeReference<>() {
        };

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get("/api/artists/" + id);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        ArtistResponseDTO foundArtist = new ObjectMapper()
                .readValue(mvcResult.getResponse().getContentAsString(), typeRef);

        assertEquals(artistResponseDTO.artistName(), foundArtist.artistName(),
                () -> "The returned artist name is incorrect");
        assertEquals(artistResponseDTO.spotifyId(), foundArtist.spotifyId(),
                () -> "The returned spotifyId is incorrect");
    }
}

package com.example.festisounds.Modules.Festival.Controllers;

import com.example.festisounds.Modules.Festival.Controller.FestivalController;
import com.example.festisounds.Modules.Festival.DTO.FestivalRequestDTO;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FestivalController.class)
@MockBean({FestivalService.class})
public class FestivalControllerTest {

    @Autowired
    FestivalService festivalService;

    @Autowired
    MockMvc mockMvc;

    private final String REQUEST_BUILDER_ENDPOINT = "/api/festivals";

    UUID festivalUUID = UUID.randomUUID();

    ArtistResponseDTO artistResponseDTO;
    FestivalRequestDTO festivalRequestDTO;
    FestivalResponseDTO festivalResponseDTO;

    @BeforeEach
    void setup() {
        artistResponseDTO = new ArtistResponseDTO(UUID.randomUUID(), "ArtistSpotifyId", "ArtistName", Set.of("Genre1", "Genre2"), Set.of(festivalUUID));

        festivalRequestDTO = FestivalRequestDTO.builder()
                .name("TestFestival1")
                .startDate(new Date())
                .endDate(new Date())
                .details("TestDetails")
                .artists(Set.of("ArtistName"))
                .city("TestCity")
                .country("TestCountry")
                .organizer("TestOrganizer")
                .website("www.testWebsite.com")
                .build();

        festivalResponseDTO = FestivalResponseDTO.builder()
                .id(festivalUUID)
                .name(festivalRequestDTO.name())
                .startDate(festivalRequestDTO.startDate())
                .endDate(festivalRequestDTO.endDate())
                .details(festivalRequestDTO.details())
                .artists(Set.of(artistResponseDTO))
                .city(festivalRequestDTO.city())
                .country(festivalRequestDTO.country())
                .organizer(festivalRequestDTO.organizer())
                .website(festivalRequestDTO.website())
                .build();

    }

    @Test
    @DisplayName("Festival can be created.")
    public void testCreateFestival_whenValidFestivalDetailsProvided_returnsCreatedFestivalDetails() throws Exception {
//        Arrange
        when(festivalService.createFestival(any(FestivalRequestDTO.class))).thenReturn(festivalResponseDTO);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(REQUEST_BUILDER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(festivalRequestDTO));

//        Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        FestivalResponseDTO createdFestival = new ObjectMapper().readValue(responseBodyAsString, FestivalResponseDTO.class);

//        Assert
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Incorrect response status.");
        assertEquals(festivalRequestDTO.name(), createdFestival.name(), "The created festival's name is incorrect.");
        assertNotNull(createdFestival.id(), "The created festival's UUID should not be null");
    }

}

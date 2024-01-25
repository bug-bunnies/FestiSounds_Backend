package com.example.festisounds.Modules.Festival.Controllers;

import com.example.festisounds.Modules.Festival.Controller.FestivalController;
import com.example.festisounds.Modules.Festival.DTO.FestivalRequestDTO;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = FestivalController.class)
@MockBean({FestivalService.class})
public class FestivalControllerTest {

    private final String REQUEST_BUILDER_ENDPOINT = "/api/festivals";
    @Autowired
    FestivalService festivalService;
    @Autowired
    MockMvc mockMvc;
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


    @Test
    @DisplayName("Festivals can be found when existing")
    public void testGetAllFestivals_whenFestivalsExist_returnsArrayOfFestivalDetails() throws Exception {
//        Arrange
        FestivalResponseDTO[] savedFestivals = new FestivalResponseDTO[]{festivalResponseDTO, festivalResponseDTO};
        int savedFestivalsSize = savedFestivals.length;

        when(festivalService.getAllFestivals()).thenReturn(savedFestivals);

//        Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(REQUEST_BUILDER_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        TypeReference<FestivalResponseDTO[]> typeRef = new TypeReference<>() {
        };
        FestivalResponseDTO[] resultFestivals = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), typeRef);

//        Assert
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Incorrect response status.");
        assertEquals(savedFestivalsSize, resultFestivals.length, "Should return a length of 2, but instead it was: " + savedFestivalsSize);
    }

    //    todo: Disabled until error handler and Http codes are updated/implemented
    @Disabled
    @Test
    @DisplayName("Festivals returns 404 when not existing.")
    void testGetAllFavouriteCocktails_whenValidCocktailDetailsDoNotExist_returnsNotFound() throws Exception {
//        Arrange
        FestivalResponseDTO[] savedFestivals = new FestivalResponseDTO[]{};

        when(festivalService.getAllFestivals()).thenReturn(savedFestivals);

//        Act
        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(REQUEST_BUILDER_ENDPOINT);

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String resultCocktails = mvcResult.getResponse().getContentAsString();

//        Assert
        assertEquals(HttpStatus.NOT_FOUND.value(), mvcResult.getResponse().getStatus(), "Incorrect Response Status");

        assertTrue(resultCocktails.isEmpty(), "Incorrect list size");
    }

    @Test
    @DisplayName("Festival can be found by valid ID.")
    public void testGetFestivalById_whenFestivalExists_returnsFestivalDetails() throws Exception {
//        Arrange
        when(festivalService.getFestivalById(festivalUUID)).thenReturn(festivalResponseDTO);
        TypeReference<FestivalResponseDTO> typeRef = new TypeReference<>() {
        };

//        Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(REQUEST_BUILDER_ENDPOINT + "/search/id/" + festivalUUID)).andReturn();

        FestivalResponseDTO resultFestival = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), typeRef);

//        Assert
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Incorrect Response Status");
        assertEquals(festivalUUID, resultFestival.id(), "Incorrect UUID for the returned festival");
    }

    @Test
    @DisplayName("Festival can be found by valid name.")
    public void testGetFestivalByName_whenFestivalExists_returnsFestivalDetails() throws Exception {
//        Arrange
        String searchQuery = "TestFestival";

        FestivalResponseDTO[] savedFestivals = new FestivalResponseDTO[]{festivalResponseDTO, festivalResponseDTO};
        when(festivalService.getFestivalsByName(searchQuery)).thenReturn(savedFestivals);
        TypeReference<FestivalResponseDTO[]> typeRef = new TypeReference<>() {};

//        Act
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(REQUEST_BUILDER_ENDPOINT + "/search/name/" + searchQuery)).andReturn();

        FestivalResponseDTO[] resultFestivals = new ObjectMapper().readValue(mvcResult.getResponse().getContentAsString(), typeRef);

//        Assert
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "Incorrect response status.");
        assertEquals(savedFestivals.length, resultFestivals.length, "Should return a length of 2, but instead it was: " + savedFestivals.length);
    }
}

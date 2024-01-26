package com.example.festisounds.Modules.Festival.Controller;

import com.example.festisounds.Core.Exceptions.Festival.FestivalNotFoundException;
import com.example.festisounds.Modules.Festival.DTO.FestivalRequestDTO;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FestivalController {
    public final static String BASE_PATH = "/api/festivals";
    public final static String BASE_PATH_SEARCH = BASE_PATH + "/search/";

    private final FestivalService festivalService;

    @PostMapping(BASE_PATH)
    public ResponseEntity<FestivalResponseDTO> createFestival(@RequestBody FestivalRequestDTO festival) {
        return ResponseEntity.ok(festivalService.createFestival(festival));
    }

    @GetMapping(BASE_PATH)
    public ResponseEntity<FestivalResponseDTO[]> getAllFestivals() {
        FestivalResponseDTO[] festivals = festivalService.getAllFestivals();
        if (festivals.length == 0) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(festivals);
        }
    }

    @GetMapping(BASE_PATH_SEARCH + "id/{festivalId}")
    public ResponseEntity<FestivalResponseDTO> getFestivalById(@PathVariable("festivalId") UUID festivalId) {
        try {
            return ResponseEntity.ok(festivalService.getFestivalById(festivalId));
        } catch (FestivalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(BASE_PATH_SEARCH + "name/{festivalName}")
    public ResponseEntity<FestivalResponseDTO[]> searchFestivalsByName(@PathVariable("festivalName") String festivalName) {
        try {
            return ResponseEntity.ok(festivalService.getFestivalsByName(festivalName));
        } catch (FestivalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(BASE_PATH + "{festivalId}")
    public ResponseEntity<String> deleteFestival(@PathVariable("festivalId") UUID festivalId) {
        try {
            festivalService.deleteFestival(festivalId);
            return ResponseEntity.ok("Festival deleted successfully");
        } catch (FestivalNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}

package com.example.festisounds.Modules.Festival.Controller;

import com.example.festisounds.Modules.Festival.DTO.FestivalRequestDTO;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FestivalController {
    public final static String BASE_PATH = "/api/festivals";
    public final static String BASE_PATH_ID = BASE_PATH + "/{festivalId}";
    public final static String BASE_PATH_SEARCH = BASE_PATH + "/search";

    private final FestivalService festivalService;

    @GetMapping(BASE_PATH)
    public ResponseEntity<FestivalResponseDTO[]> getAllFestivals() {
        return ResponseEntity.ok(festivalService.getAllFestivals());
    }

    @GetMapping(BASE_PATH_ID)
    public ResponseEntity<FestivalResponseDTO> getFestivalById(@PathVariable("festivalId") UUID festivalId) {
        try {
            return ResponseEntity.ok(festivalService.getFestivalById(festivalId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(BASE_PATH_SEARCH)
    public ResponseEntity<FestivalResponseDTO[]> searchFestivalsByName(@RequestParam("festivalName") String festivalName) {
        try {
            return ResponseEntity.ok(festivalService.getFestivalsByName(festivalName));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping(BASE_PATH)
    public ResponseEntity<FestivalResponseDTO> createFestival(@RequestBody FestivalRequestDTO festival) {
        return ResponseEntity.ok(festivalService.createFestival(festival));
    }

    @DeleteMapping(BASE_PATH_ID)
    public ResponseEntity<String> deleteFestival(@PathVariable("festivalId") UUID festivalId) {
        try {
            festivalService.delete(festivalId);
            return ResponseEntity.ok("Festival deleted successfully");
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().header(e.getMessage()).build();
        }
    }

}

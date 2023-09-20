package com.example.festisounds.Modules.Festival.Controller;

import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class FestivalController {
public final static String BASE_PATH = "/api/festivals";
    public final static String BASE_PATH_ID = BASE_PATH + "/{festivalId}";

    private final FestivalRepo festivalRepo;
    private final FestivalService festivalService;

    @GetMapping(BASE_PATH)
    public ResponseEntity<List<Festival>> getAllFestivals() {
        return ResponseEntity.ok(festivalRepo.findAll());
    }

    @GetMapping(BASE_PATH_ID)
    public ResponseEntity<Optional<Festival>> getFestivalById(@PathVariable("festivalId") UUID festivalId) {
        return ResponseEntity.ok(festivalRepo.findById(festivalId));
    }

    @PostMapping(BASE_PATH)
    public ResponseEntity<Festival> createFestival(@RequestBody Festival festival) {
        return ResponseEntity.ok(festivalService.createFestival(festival));
    }

    @DeleteMapping(BASE_PATH_ID)
    public ResponseEntity<String> deleteFestival(@PathVariable("festivalId") UUID festivalId) {
        festivalRepo.deleteById(festivalId);
        return ResponseEntity.ok("Festival deleted successfully");
    }

}

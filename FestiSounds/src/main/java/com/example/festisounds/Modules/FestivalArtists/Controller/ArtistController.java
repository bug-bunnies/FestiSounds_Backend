package com.example.festisounds.Modules.FestivalArtists.Controller;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.FestivalArtists.Service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/artists/")
public class ArtistController {
    private final ArtistService service;

    @GetMapping("find")
    @Operation(description = "Get artists by name", summary = "Returns list of artist from spotify api")
    public ResponseEntity<Artist[]> getAllArtist(@RequestParam String artistName) {
        return ResponseEntity.ok(service.getSpotifyArtistData(artistName));
    }

    @GetMapping("{artistId}")
    @Operation(description = "get artist by id", summary = "Returns artist's data from festiSounds DB")
    public ResponseEntity<ArtistDTO> findArtist(@PathVariable UUID artistId) {
        try {
            return ResponseEntity.ok(service.getArtist(artistId));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("new")
    public ResponseEntity.BodyBuilder createArtist(@RequestBody String artistName, UUID festivalId) {
        String[] artists = artistName.split(",");
        for (String name : artists) {
            service.createArtist(name, festivalId);
        }
        return ResponseEntity.status(200);
    }

    @PutMapping("festival")
    public ResponseEntity<String> updateFestival(@RequestBody String artistName, UUID festivalId) {
        return ResponseEntity.ok(service.addArtistToFestival(artistName, festivalId));
    }

    @PutMapping("genres")
    public ResponseEntity.BodyBuilder updateGenres(@RequestBody String artistName) throws Exception {
        String[] artists = artistName.split(",");
//        ArrayList<ArtistDTO> list = new ArrayList<>();
        for (String name : artists) {
           service.updateArtistGenres(name.trim());
//           list.add(updatedArtist);
        }
        return ResponseEntity.status(200);
    }

}

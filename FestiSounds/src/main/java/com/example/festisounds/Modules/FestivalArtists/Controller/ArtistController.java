package com.example.festisounds.Modules.FestivalArtists.Controller;

import com.example.festisounds.Core.Exceptions.FestivalArtists.ArtistNotFoundException;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.sql.SQLException;
import java.util.*;

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
    public ResponseEntity<ArtistResponseDTO> findArtist(@PathVariable UUID artistId) {
        try {
            return ResponseEntity.ok(service.getArtist(artistId));
        } catch (ArtistNotFoundException e) {
            return ResponseEntity.status(404).build();
        }
    }

    // TODO: Return a body, not just the status!!!
    @PostMapping("new")
    public ResponseEntity<List<ArtistResponseDTO>> createArtist(@RequestParam String artistName, @RequestParam UUID festivalId) throws SQLException {
        String[] artistNames = artistName.split(",");
        List<ArtistResponseDTO> artists = new ArrayList<>();
        for (String name : artistNames) {
            ArtistResponseDTO artist = service.createOrAddArtistRouter(name, festivalId);
            artists.add(artist);
        }
        return ResponseEntity.status(200).body(artists);
    }

    @PutMapping("festival")
    public ResponseEntity<ArtistResponseDTO> updateFestivalArtist(@RequestParam String artistName, @RequestParam UUID festivalId) throws SQLException {
        return ResponseEntity.ok(service.createOrAddArtistRouter(artistName, festivalId));
    }

    @PutMapping("genres")
    public ResponseEntity<Set<String>> updateGenres(@RequestBody String artistName) throws Exception {
        String[] artists = artistName.split(",");
        Set<String> artistGenres = new HashSet<>();
        for (String name : artists) {
            artistGenres = service.updateArtistGenres(name.trim());
            System.out.println(name + " artist name~asz");
        }
        return ResponseEntity.status(200).body(artistGenres);
    }

    @DeleteMapping("{artistId}")
    public ResponseEntity.HeadersBuilder deleteArtist(@PathVariable UUID artistId) {
        try {
            ArtistResponseDTO artist = service.getArtist(artistId);
            if (artist != null)
                service.deleteArtist(artistId);
            return ResponseEntity.status(200);
        } catch (Exception e) {
            return ResponseEntity.status(404);
        }
    }

}

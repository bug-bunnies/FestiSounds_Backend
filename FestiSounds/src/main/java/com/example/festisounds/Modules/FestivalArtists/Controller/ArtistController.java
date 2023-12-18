package com.example.festisounds.Modules.FestivalArtists.Controller;


import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.FestivalArtists.Service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.ArrayList;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService service;
//    UUID festivalId = UUID.fromString("833ffa35-e2cd-4fb1-a564-7e25a46e85c8");
    @GetMapping("/api/getArtist")
    public ResponseEntity<Artist[]> getAllArtist(@RequestParam String artistName) {
        return ResponseEntity.ok(service.getSpotifyArtistData(artistName));
    }

    @PostMapping("/api/artists/new")
    public ResponseEntity.BodyBuilder createArtist(@RequestBody String artistName, UUID festivalId) {
        String[] artists = artistName.split(",");
        for (String name : artists) {
            service.createArtist(name, festivalId);
        }
        return ResponseEntity.status(200);
    }

    @PutMapping("/api/artists/genres")
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

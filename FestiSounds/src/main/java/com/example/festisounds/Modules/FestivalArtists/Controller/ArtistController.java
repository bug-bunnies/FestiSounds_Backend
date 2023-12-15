package com.example.festisounds.Modules.FestivalArtists.Controller;


import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.Festival.Service.FestivalService;
import com.example.festisounds.Modules.FestivalArtists.Service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ArtistController {
    private final ArtistService service;
    UUID festivalId = UUID.fromString("833ffa35-e2cd-4fb1-a564-7e25a46e85c8");

    @PostMapping("/api/artists/new")
    public ResponseEntity.BodyBuilder createArtist(@RequestBody String artistName) {
        String[] artists = artistName.split(",");
        for (String name : artists) {
            service.createArtist(name, festivalId);
        }
        return ResponseEntity.status(200);
    }



    @GetMapping("/api/getArtist")
    public ResponseEntity<String> getAllArtist(@RequestParam String artistName) {
        return ResponseEntity.ok(service.getSpotifyId(artistName));
    }

}

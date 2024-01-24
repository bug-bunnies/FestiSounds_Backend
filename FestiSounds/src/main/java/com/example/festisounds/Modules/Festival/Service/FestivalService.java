package com.example.festisounds.Modules.Festival.Service;

import com.example.festisounds.Modules.Festival.DTO.FestivalRequestDTO;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.*;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.festisounds.Modules.Festival.Service.FestivalDTOBuilder.artistDTOBuilder;
import static com.example.festisounds.Modules.Festival.Service.FestivalDTOBuilder.festivalEntityBuilder;

@Service
@RequiredArgsConstructor
public class FestivalService {
    private final FestivalRepo festivalRepo;
    private final ArtistService artistService;


    //    TODO: Check closing of stream
    public FestivalResponseDTO createFestival(FestivalRequestDTO festival) {
        Festival storedFestival = festivalRepo.save(festivalEntityBuilder(festival));
        Set<ArtistResponseDTO> newArtists = new HashSet<>();
        if (!festival.artists().isEmpty()) {
             newArtists = festival.artists().stream()
                    .peek(System.out::println)
                    .map(name -> artistService.createOrAddArtistRouter(name, storedFestival.getId()))
                    .collect(Collectors.toSet());
             return FestivalDTOBuilder.festivalDTOBuilder(storedFestival, newArtists);
        }
        return FestivalDTOBuilder.festivalDTOBuilder(storedFestival);
    }

    public FestivalResponseDTO[] getAllFestivals() {
        List<Festival> festivals = festivalRepo.findAll();
        return festivals.stream()
                .map(FestivalDTOBuilder::festivalDTOBuilder)
                .collect(Collectors.toList()).toArray(new FestivalResponseDTO[0]);
    }

    public FestivalResponseDTO getFestivalById(UUID id) {
        Festival festivalById = festivalRepo.findById(id).orElseThrow();
        return FestivalDTOBuilder.festivalDTOBuilder(festivalById);
    }

    // needs to check for not matching names
    public FestivalResponseDTO[] getFestivalsByName(String name) {
        List<Festival> festivalsByName = festivalRepo.findByNameContainingIgnoreCase(name);
        return festivalsByName.stream()
                .map(FestivalDTOBuilder::festivalDTOBuilder)
                .collect(Collectors.toList()).toArray(new FestivalResponseDTO[0]);
    }

    //    TODO: Cannot currently delete if artists are linked.
    public void delete(UUID id) {
        if (festivalRepo.findById(id).isPresent()) {
            festivalRepo.deleteById(id);
        } else {
            throw new NotFoundException("Festival not found!");
        }
    }


}

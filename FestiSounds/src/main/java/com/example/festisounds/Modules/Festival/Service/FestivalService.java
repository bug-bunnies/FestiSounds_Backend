package com.example.festisounds.Modules.Festival.Service;

import com.example.festisounds.Core.Exceptions.Festival.FestivalNotFoundException;
import com.example.festisounds.Modules.Festival.DTO.FestivalRequestDTO;
import com.example.festisounds.Modules.Festival.DTO.FestivalResponseDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.festisounds.Modules.Festival.Service.FestivalDTOBuilder.festivalEntityBuilder;

@Service
@RequiredArgsConstructor
public class FestivalService {
    private final FestivalRepo festivalRepo;
    private final ArtistService artistService;


    public FestivalResponseDTO createFestival(FestivalRequestDTO festival) {
        Festival storedFestival = festivalRepo.save(festivalEntityBuilder(festival));
        Set<ArtistResponseDTO> newArtists = new HashSet<>();
//        TODO: Look at moving this artist logic to a check in the controller and pass workload to artistService to de-couple both layers.
        if (!festival.artists().isEmpty()) {
            newArtists = festival.artists().stream()
                    .peek(System.out::println)
                    .map(name -> artistService.createOrAddArtistRouter(name, storedFestival.getId()))
                    .collect(Collectors.toSet());
            return FestivalDTOBuilder.festivalDTOBuilder(storedFestival, newArtists);
        }
        return FestivalDTOBuilder.festivalDTOBuilder(storedFestival);
    }

    public FestivalResponseDTO[] getAllFestivals() throws FestivalNotFoundException {
        List<Festival> festivals = festivalRepo.findAll();
        if (festivals.isEmpty()) {
            throw new FestivalNotFoundException("No festivals found.");
        }
        return festivals.stream()
                .map(FestivalDTOBuilder::festivalDTOBuilder)
                .toList().toArray(new FestivalResponseDTO[0]);
    }

    public FestivalResponseDTO getFestivalById(UUID festivalId) throws FestivalNotFoundException {
        Festival festivalById = festivalRepo.findById(festivalId).orElseThrow(
                () -> new FestivalNotFoundException("Could not find a festival by the id: " + festivalId)
        );
        return FestivalDTOBuilder.festivalDTOBuilder(festivalById);
    }

    // needs to check for not matching names
    public FestivalResponseDTO[] getFestivalsByName(String festivalName) throws FestivalNotFoundException {
        List<Festival> festivalsByName = festivalRepo.findByNameContainingIgnoreCase(festivalName);
        if (festivalsByName.isEmpty()) {
            throw new FestivalNotFoundException("Could not find a festival by the name: " + festivalName);
        }
        return festivalsByName.stream()
                .map(FestivalDTOBuilder::festivalDTOBuilder)
                .toList().toArray(new FestivalResponseDTO[0]);
    }

    //    TODO: Cannot currently delete if artists are linked.
    public void deleteFestival(UUID festivalId) throws FestivalNotFoundException {
        if (festivalRepo.findById(festivalId).isPresent()) {
            festivalRepo.deleteById(festivalId);
        } else {
            throw new FestivalNotFoundException("Could not delete, festivalId not found: " + festivalId);
        }
    }
}

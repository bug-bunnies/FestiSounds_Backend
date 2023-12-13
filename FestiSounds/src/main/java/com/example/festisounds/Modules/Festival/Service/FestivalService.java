package com.example.festisounds.Modules.Festival.Service;

import com.example.festisounds.Modules.Festival.DTO.FestivalDTO;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private static final String SUPABASE_URL = System.getenv("supabaseUrl");
    private static final String SUPABASE_KEY = System.getenv("supabaseKey");

    private final FestivalRepo festivalRepo;

    public Festival createFestival(Festival festival) {
        return festivalRepo.save(festival);
    }

    public FestivalDTO[] getAllFestivals() {
        List<Festival> festivals = festivalRepo.findAll();
        return festivals.stream()
                .map(FestivalDTOBuilder::festivalDataBuilder)
                .collect(Collectors.toList()).toArray(new FestivalDTO[0]);
    }

    public FestivalDTO getFestivalById(UUID id) {
        Festival festivalById = festivalRepo.findById(id).orElseThrow();
        return FestivalDTOBuilder.festivalDataBuilder(festivalById);
    }

    // needs to check for not matching names
    public FestivalDTO[] getFestivalsByName(String name) {
        List<Festival> festivalsByName = festivalRepo.findByNameContainingIgnoreCase(name);
        return festivalsByName.stream()
                .map(FestivalDTOBuilder::festivalDataBuilder)
                .collect(Collectors.toList()).toArray(new FestivalDTO[0]);
    }

    public void delete(UUID id) {
        if (festivalRepo.findById(id).isPresent()) {
            festivalRepo.deleteById(id);
        } else {
            throw new NotFoundException("Festival not found!");
        }
    }


}

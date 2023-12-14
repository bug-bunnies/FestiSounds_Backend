package com.example.festisounds.Modules.Festival.Service;

import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private final FestivalRepo festivalRepo;

    public Festival createFestival(Festival festival) {
        return festivalRepo.save(festival);
    }
}

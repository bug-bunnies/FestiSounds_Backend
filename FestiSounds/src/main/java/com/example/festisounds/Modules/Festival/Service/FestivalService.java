package com.example.festisounds.Modules.Festival.Service;

import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FestivalService {

    private static final String SUPABASE_URL = System.getenv("supabaseUrl");
    private static final String SUPABASE_KEY = System.getenv("supabaseKey");

    private final FestivalRepo festivalRepo;

    public Festival createFestival(Festival festival) {
        return festivalRepo.save(festival);
    }
}

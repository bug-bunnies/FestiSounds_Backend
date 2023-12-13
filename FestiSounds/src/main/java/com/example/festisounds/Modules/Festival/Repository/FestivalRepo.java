package com.example.festisounds.Modules.Festival.Repository;

import com.example.festisounds.Modules.Festival.Entities.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FestivalRepo extends JpaRepository<Festival, UUID> {
    List<Festival> queryFestivalsByName(String name);
}

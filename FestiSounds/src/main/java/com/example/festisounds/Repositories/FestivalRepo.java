package com.example.festisounds.Repositories;

import com.example.festisounds.Entites.Festival;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FestivalRepo extends JpaRepository<Festival, UUID> {

}

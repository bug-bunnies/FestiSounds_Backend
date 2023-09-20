package com.example.festisounds.Modules.FestiSoundArtists.Repositories;

import com.example.festisounds.Modules.FestiSoundArtists.Entities.FestiSoundArtist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FestiSoundArtistRepository extends JpaRepository<FestiSoundArtist, UUID> {
}

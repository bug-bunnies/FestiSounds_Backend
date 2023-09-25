package com.example.festisounds.Modules.FestivalArtists.Repositories;

import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FestivalArtistRepository extends JpaRepository<FestivalArtist, UUID> {
}

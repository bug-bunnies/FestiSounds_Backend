package com.example.festisounds.Modules.UserData.DTOs;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;

import java.util.ArrayList;

public record RecommendedArtistsDTO(ArrayList<ArtistResponseDTO> fullArtistsList,
                                    ArrayList<ArtistResponseDTO> knownArtistsList,
                                    ArrayList<ArtistResponseDTO> newArtistsList) {
}

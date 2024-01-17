package com.example.festisounds.Modules.UserData.DTOs;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;

import java.util.ArrayList;

public record RecommendedArtistsDTO(ArrayList<ArtistDTO> fullArtistsList,
                                    ArrayList<ArtistDTO> knownArtistsList,
                                    ArrayList<ArtistDTO> newArtistsList) {
}

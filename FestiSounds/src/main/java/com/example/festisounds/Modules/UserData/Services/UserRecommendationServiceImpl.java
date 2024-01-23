package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.UserData.DTOs.RecommendedArtistsDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

@Service
public class UserRecommendationServiceImpl implements UserRecommendationService {

    @Autowired
    UserArtistMatchingServiceImpl matchingService;

    @Autowired
    CacheManager cacheManager;

    @Override
    public RecommendedArtistsDTO recommendArtists(UUID festivalId) throws IOException, ParseException, SpotifyWebApiException {
        LinkedHashMap<ArtistResponseDTO, Double> rankedFestivalArtists = matchingService.getArtistRankingFromFestival(festivalId);
        Cache cachedArtists = cacheManager.getCache("user-top-artists");
        ArrayList<String> artistData = cachedArtists.get("cacheUserArtistData", ArrayList.class);

        ArrayList<ArtistResponseDTO> fullArtistList = new ArrayList<>();
        ArrayList<ArtistResponseDTO> knownArtistList = new ArrayList<>();
        ArrayList<ArtistResponseDTO> newArtistList = new ArrayList<>();

        for (ArtistResponseDTO artist : rankedFestivalArtists.keySet()) {
            fullArtistList.add(artist);

            for (String spotifyArtistId : artistData) {
                if (artist.spotifyId().equals(spotifyArtistId)) {
                    knownArtistList.add(artist);
                } else {
                    newArtistList.add(artist);
                }
            }
        }

        return new RecommendedArtistsDTO(fullArtistList, knownArtistList, newArtistList);
    }
}

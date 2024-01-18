package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.UserData.DTOs.RecommendedArtistsDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserRecommendationServiceImpl implements UserRecommendationService {

    @Autowired
    UserArtistMatchingServiceImpl matchingService;

    @Autowired
    CacheManager cacheManager;

    @Override
    public RecommendedArtistsDTO recommendArtists(UUID festivalId) throws IOException, ParseException, SpotifyWebApiException {
        LinkedHashMap<ArtistDTO, Double> rankedFestivalArtists = matchingService.getArtistRankingFromFestival(festivalId);
        Cache cachedArtists = cacheManager.getCache("user-top-artists");
        ArrayList<String> artistData = cachedArtists.get("cacheUserArtistData", ArrayList.class);

        ArrayList<ArtistDTO> fullArtistList = new ArrayList<>();
        ArrayList<ArtistDTO> knownArtistList = new ArrayList<>();
        ArrayList<ArtistDTO> newArtistList = new ArrayList<>();

        for (ArtistDTO artist : rankedFestivalArtists.keySet()) {
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

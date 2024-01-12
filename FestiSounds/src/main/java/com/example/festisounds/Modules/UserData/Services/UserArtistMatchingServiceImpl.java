package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.SimpleKey;


import java.io.IOException;
import java.util.HashMap;

@Service
public class UserArtistMatchingServiceImpl implements UserArtistMatchingService {

    @Autowired
    CacheManager cacheManager;
    @Autowired
    private UserProcessingServiceImpl userProcessingService;
    @Override
    public HashMap<ArtistDTO, Double> getArtistRankingFromFestival(String festivalId)
            throws IOException, ParseException, SpotifyWebApiException {

        HashMap<String, Double> genreData;

        Cache cachedGenres = cacheManager.getCache("user-genre-data");
        if (cachedGenres != null) {
            genreData = cachedGenres.get(new SimpleKey(), HashMap.class);
        } else {
            genreData = userProcessingService.rankUsersFavouriteGenres();
        }

        return null;
    }
}

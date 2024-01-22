package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Core.Utils.GenrePositionMapGenerator;
import org.springframework.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserCachingServiceImpl implements UserCachingService {

    @Autowired
    CacheManager cacheManager;
    @Override
    public void evictAllCaches() {
            cacheManager.getCacheNames()
                    .forEach(cacheName -> Objects.requireNonNull(cacheManager.getCache(cacheName))
                            .clear());
    }

    @Cacheable(value = "user-top-artists", key = "#root.method.name")
    @Override
    public ArrayList<String> cacheUserArtistData(Artist[]... artistArrayData){
        return Arrays
                .stream(artistArrayData)
                .flatMap(Arrays::stream)
                .map(Artist::getId)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Cacheable(value = "genre-position-data", key = "#fileName")
    public HashMap<String, short[]> makeTheMap(String fileName) {
        return new GenrePositionMapGenerator().makeGenrePositionMap(fileName);
    }
}

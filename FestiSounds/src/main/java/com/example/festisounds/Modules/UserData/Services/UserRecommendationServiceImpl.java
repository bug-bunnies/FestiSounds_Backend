package com.example.festisounds.Modules.UserData.Services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class UserRecommendationServiceImpl implements UserRecommendationService {

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
}

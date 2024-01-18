package com.example.festisounds.Modules.UserData.Services;

import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.util.ArrayList;

public interface UserCachingService {
    void evictAllCaches();

    ArrayList<String> cacheUserArtistData(Artist[]... artistArrayData);
}

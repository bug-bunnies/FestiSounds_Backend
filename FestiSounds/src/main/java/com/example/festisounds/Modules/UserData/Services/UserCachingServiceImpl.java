package com.example.festisounds.Modules.UserData.Services;

import org.springframework.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
}

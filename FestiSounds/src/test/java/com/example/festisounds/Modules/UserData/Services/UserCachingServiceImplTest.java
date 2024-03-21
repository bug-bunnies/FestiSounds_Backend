package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.FestiSoundsApplication;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {FestiSoundsApplication.class})
@ContextConfiguration
class UserCachingServiceImplTest {


    @Autowired
    CacheManager cacheManager;
    @Autowired
    UserCachingServiceImpl userCachingService;

    @BeforeEach
    void init() {
        userCachingService.buildAndCacheGenrePositionMap("Genre3DMap.csv");
    }

    @ParameterizedTest
    @MethodSource("genrePositionQueryParameters")
    public void testMakeGenrePositionMap_whenCacheQueried_GivesCorrectGenrePosition(String genre, short[] expectedPosition) {
        Cache cachedGenreMap = cacheManager.getCache("genre-position-data");
        HashMap<String, short[]> genrePositionMapCached = cachedGenreMap.get("Genre3DMap.csv", HashMap.class);

        assertNotNull(genrePositionMapCached);

        short[] positionResult = genrePositionMapCached.get(genre);

        assertArrayEquals(expectedPosition, positionResult,
                () -> "Position map returns position of " + Arrays.toString(positionResult) + " instead of " + Arrays.toString(expectedPosition));
    }


    public static Stream<Arguments> genrePositionQueryParameters() {
        return Stream.of(
                Arguments.of("deep disco house", new short[]{1359, 18794, 169, 141, 167}),
                Arguments.of("indie folk italiano", new short[]{680, 9698, 128, 146, 23}),
                Arguments.of("swamp rock", new short[]{793, 8713, 159, 120, 22}),
                Arguments.of("british classical piano", new short[]{476, 1007, 32, 161, 198}),
                Arguments.of("kentucky indie", new short[]{533, 9673, 148, 137, 27}),
                Arguments.of("j-core", new short[]{571, 17542, 218, 109, 98})
        );
    }

}
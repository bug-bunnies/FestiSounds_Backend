package com.example.festisounds.Core.Utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//@RunWith(SpringRunner.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class GenrePositionMapGeneratorTest {
    static GenrePositionMapGenerator genreMapGenerator;
    static HashMap<String, short[]> genrePositionMap;

    static Cache cachedGenreMap;

//    @Autowired
//    private CacheManager cacheManager;
    @BeforeAll
    static void init() {
        genreMapGenerator = new GenrePositionMapGenerator();
        genrePositionMap = genreMapGenerator.makeGenrePositionMap("Genre3DMap.csv");
    }

    @Test
    public void testMakeGenrePositionMap_whenCalled_buildsMapOfCorrectSize() {
        int expectedMapLength = 5453;

        assertNotNull(genrePositionMap);
        assertEquals(expectedMapLength, genrePositionMap.size());

    }

    @ParameterizedTest
    @MethodSource("hexConvertToRgbTest")
    public void testConvertHexColourToRGB_whenGivenHexColour_returnsRgbShortArray(String hexColour, short[] expectedResult) {
        short[] testResult = genreMapGenerator.convertHexColourToRGB(hexColour);

        assertArrayEquals(expectedResult, testResult, () -> Arrays.toString(expectedResult) + " does not match " + Arrays.toString(testResult));
    }

    static Stream<Arguments> hexConvertToRgbTest() {
        return Stream.of(
                Arguments.of("#FF9933", new short[]{255, 153, 51}),
                Arguments.of("#1ba3d7", new short[]{27, 163, 215}),
                Arguments.of("#e1531d", new short[]{225, 83, 29}),
                Arguments.of("#2f885b", new short[]{47, 136, 91}),
                Arguments.of("#91863d", new short[]{145, 134, 61}),
                Arguments.of("#9a8b14", new short[]{154, 139, 20})
        );
    }


    @ParameterizedTest
    @MethodSource("genrePositionQueryParameters")
    public void testMakeGenrePositionMap_whenQueried_GivesCorrectGenrePosition(String genre, short[] expectedPosition) {
        short[] positionResult = genrePositionMap.get(genre);

        assertArrayEquals(expectedPosition, positionResult,
                () -> "Position map returns position of " + Arrays.toString(positionResult) + " instead of " + Arrays.toString(expectedPosition));
    }

//    @ParameterizedTest
//    @MethodSource("genrePositionQueryParameters")
//    public void testMakeGenrePositionMap_whenCacheQueried_GivesCorrectGenrePosition(String genre, short[] expectedPosition) {
//        Cache cachedGenreMap = cacheManager.getCache("genre-position-data");
//        HashMap<String, short[]> genrePositionMapCached = cachedGenreMap.get("makeGenrePositionMap", HashMap.class);
//
//        assertNotNull(genrePositionMapCached);
//
//        short[] positionResult = genrePositionMapCached.get(genre);
//
//        assertArrayEquals(expectedPosition, positionResult,
//                () -> "Position map returns position of " + Arrays.toString(positionResult) + " instead of " + Arrays.toString(expectedPosition));
//    }


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
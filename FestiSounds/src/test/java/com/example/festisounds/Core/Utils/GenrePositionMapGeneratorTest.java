package com.example.festisounds.Core.Utils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import se.michaelthelin.spotify.model_objects.specification.Artist;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;


class GenrePositionMapGeneratorTest {
    static GenrePositionMapGenerator genreMapGenerator;
    static HashMap<String, short[]> genrePositionMap;

    @BeforeAll
    static void init() {
        genreMapGenerator  = new GenrePositionMapGenerator();
        genrePositionMap = genreMapGenerator.makeGenrePositionMap("Genre3DMap.csv");
    }

    @Test
    public void testMakeGenrePositionMap_whenCalled_buildsMapOfCorrectSize() {
        int expectedMapLength = 5453;

        assertNotNull(genrePositionMap);
        assertEquals(expectedMapLength, genrePositionMap.size());

    }

//    @ParameterizedTest





}
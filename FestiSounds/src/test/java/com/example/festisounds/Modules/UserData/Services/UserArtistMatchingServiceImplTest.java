package com.example.festisounds.Modules.UserData.Services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class UserArtistMatchingServiceImplTest {

    @ParameterizedTest
    @MethodSource("artistScoreParameters")
    void testGetArtistScore_WhenProvidedValidScores_ReturnsCorrectResult(Double expectedResult, ArrayList<Double> genreScores) {

        Double actualResult = UserArtistMatchingServiceImpl.getArtistScore(genreScores);

        assertEquals(expectedResult, (double) Math.round(actualResult * 100) /100,
                "Artist score of " + actualResult + " was returned instead of " + expectedResult);
    }
    public static Stream<Arguments> artistScoreParameters() {
        return Stream.of(
                Arguments.of(99.4, new ArrayList<>(Arrays.asList(90.0, 80.0, 70.0))),
                Arguments.of(70.25, new ArrayList<>(Arrays.asList(50.0, 30.0, 15.0))),
                Arguments.of(93.07, new ArrayList<>(Arrays.asList(87.4, 34.8, 15.6))),
                Arguments.of(93.14, new ArrayList<>(Arrays.asList(93.0, 2.0))),
                Arguments.of(4.0, new ArrayList<>(List.of(4.0)))
                );
    }
}
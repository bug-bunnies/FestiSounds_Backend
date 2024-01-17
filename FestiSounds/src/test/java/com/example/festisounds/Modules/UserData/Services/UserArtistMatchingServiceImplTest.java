package com.example.festisounds.Modules.UserData.Services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;


class UserArtistMatchingServiceImplTest {

    UserArtistMatchingServiceImpl userArtistMatchingService = new UserArtistMatchingServiceImpl();

    @DisplayName("Correct Genre Scores")
    @ParameterizedTest
    @MethodSource("genreScoreParameters")
    void testGetGenreScore_WhenProvidedData_ReturnsCorrectResults(HashMap<String, Double> genreData, Set<String> artistGenres, ArrayList<Double> expectedResult) {
        ArrayList<Double> genreScores = userArtistMatchingService.getGenreScore(genreData, artistGenres);

        assertEquals(expectedResult, genreScores,
                "Genre score should have been " + expectedResult.toString() + " but was " + genreScores.toString());
        System.out.println(genreScores);
    }

    // TODO: Full test courage of this method (edge cases and exception handling)
    @ParameterizedTest
    @MethodSource("artistScoreParameters")
    void testGetArtistScore_WhenProvidedValidScores_ReturnsCorrectResult(Double expectedResult, ArrayList<Double> genreScores) {

        Double actualResult = userArtistMatchingService.getArtistScore(genreScores);

        assertEquals(expectedResult, (double) Math.round(actualResult * 100) / 100,
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

    public static Stream<Arguments> genreScoreParameters() {
        HashMap<String, Double> maxGenreMap = readMapCsv("maxFirstGenreData.csv");
        HashMap<String, Double> iliGenreMap = readMapCsv("ilianaGenreData.csv");
        ArrayList<Set<String>> genreSets = readSetCsv("ArtistGenreSet.csv");

        // TODO: Try and generate with loop or stream - or is there a better way?
        return Stream.of(
                Arguments.of(maxGenreMap, genreSets.get(0), new ArrayList<Double>()),
                Arguments.of(maxGenreMap, genreSets.get(1), new ArrayList<Double>()),
                Arguments.of(maxGenreMap, genreSets.get(2), new ArrayList<Double>()),
                Arguments.of(maxGenreMap, genreSets.get(3), new ArrayList<>(Arrays.asList(28.57, 28.57, 28.57, 28.57, 28.57))),
                Arguments.of(iliGenreMap, genreSets.get(0), new ArrayList<Double>()),
                Arguments.of(iliGenreMap, genreSets.get(1), new ArrayList<Double>(List.of(36.36))),
                Arguments.of(iliGenreMap, genreSets.get(2), new ArrayList<Double>(Arrays.asList(92.6, 92.6, 84.88, 73.31, 54.02, 50.16, 42.44))),
                Arguments.of(iliGenreMap, genreSets.get(3), new ArrayList<Double>())
        );
    }

    @Test
    void testMapBuilding() {
        HashMap<String, Double> maxMap = readMapCsv("maxFirstGenreData.csv");
        HashMap<String, Double> iliMap = readMapCsv("ilianaGenreData.csv");
        ArrayList<Set<String>> genreSets = readSetCsv("ArtistGenreSet.csv");

        System.out.println(maxMap.toString());
        System.out.println(iliMap.toString());
        System.out.println(genreSets.toString());

    }

    public static HashMap<String, Double> readMapCsv(String fileName) {
        HashMap<String, Double> builtMap = new HashMap<>();
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            File csvFile = resource.getFile();
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            MappingIterator<String[]> iterator = mapper.readerFor(String[].class).readValues(csvFile);
            while (iterator.hasNext()) {
                String[] row = iterator.next();
                builtMap.put(row[0], Double.parseDouble(row[1]));
            }
        } catch (IOException e) {
            System.out.println("ffs");
        }
        return builtMap;
    }

    public static ArrayList<Set<String>> readSetCsv(String fileName) {
        ArrayList<Set<String>> builtSetList = new ArrayList<>();
        try {
            ClassPathResource resource = new ClassPathResource(fileName);
            File csvFile = resource.getFile();
            CsvMapper mapper = new CsvMapper();
            mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
            MappingIterator<String[]> iterator = mapper.readerFor(String[].class).readValues(csvFile);
            while (iterator.hasNext()) {
                String[] row = iterator.next();
                Set<String> nextArtistSet = new HashSet<String>(Arrays.asList(row));
                builtSetList.add(nextArtistSet);
            }
        } catch (IOException e) {
            System.out.println("ffs");
        }
        return builtSetList;
    }
}
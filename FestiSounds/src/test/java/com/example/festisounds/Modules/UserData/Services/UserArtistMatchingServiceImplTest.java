package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Core.Utils.GenrePositionMapGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import org.junit.jupiter.api.BeforeAll;
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
    static GenrePositionMapGenerator genreMapGenerator;
    static HashMap<String, short[]> genrePositionMap;

    @BeforeAll
    static void init() {
        genreMapGenerator = new GenrePositionMapGenerator();
        genrePositionMap = genreMapGenerator.makeGenrePositionMap("Genre3DMap.csv");
    }

    // TODO: Full test courage of this method (edge cases and exception handling)
    @ParameterizedTest
    @MethodSource("artistScoreParameters")
    void testGetArtistScore_WhenProvidedInputs_ReturnsResultInCorrectRange(HashMap<String, Double> genreData,
                                                                            Set<String> artistGenres) {
        double minResult = 0.0;
        double maxResult = 100.0;

        Double actualResult = userArtistMatchingService.getArtistScore(genreData, artistGenres, genrePositionMap);

        assertNotNull(actualResult);
        assertTrue(actualResult >= minResult && actualResult <= maxResult,
                () -> "Result should be in range " + minResult + "-" + maxResult + " but was " + actualResult);
    }

    public static Stream<Arguments> artistScoreParameters() {
        HashMap<String, Double> maxGenreMap = readMapCsv("maxFirstGenreData.csv");
        HashMap<String, Double> iliGenreMap = readMapCsv("ilianaGenreData.csv");
        ArrayList<Set<String>> genreSets = readSetCsv("ArtistGenreSet.csv");

        return Stream.of(
                Arguments.of(maxGenreMap, genreSets.get(0)),
                Arguments.of(maxGenreMap, genreSets.get(1)),
                Arguments.of(maxGenreMap, genreSets.get(2)),
                Arguments.of(maxGenreMap, genreSets.get(3)),
                Arguments.of(iliGenreMap, genreSets.get(0)),
                Arguments.of(iliGenreMap, genreSets.get(1)),
                Arguments.of(iliGenreMap, genreSets.get(2)),
                Arguments.of(iliGenreMap, genreSets.get(3))
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

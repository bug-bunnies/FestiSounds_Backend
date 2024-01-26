package com.example.festisounds.TestUtils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.*;

import static com.example.festisounds.TestUtils.CsvReader.readMapCsv;
import static com.example.festisounds.TestUtils.CsvReader.readSetCsv;
import static org.junit.jupiter.api.Assertions.*;

class CsvReaderTest {
// TODO: actually test the reader!


//    @ParameterizedTest
//    @CsvFileSource(resources = {"/ArtistGenreSet.csv"}, delimiter = '\n')
//    void testReadSetCsv_WhenGivenArtistGenreSet_ShouldBuildSet(String genreLine) {
//        String[] genreArray = genreLine.split((", "));
//        Set<String> genreSet = new HashSet<>(Set.of(genreArray));
//        ArrayList<Set<String>> genreSets = readSetCsv("ArtistGenreSet.csv");
//
//        ass
//    }

    @Test
    void testMapBuilding() {
        HashMap<String, Double> maxMap = readMapCsv("maxFirstGenreData.csv");
        HashMap<String, Double> iliMap = readMapCsv("ilianaGenreData.csv");
        HashMap<String, Double> robbieMap = readMapCsv("RobbieGenreData.csv");
        ArrayList<Set<String>> genreSets = readSetCsv("ArtistGenreSet.csv");

        System.out.println(maxMap.toString());
        System.out.println(iliMap.toString());
        System.out.println(genreSets.toString());

    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/RobbieGenreData.csv", "/ilianaGenreData.csv", "/maxFirstGenreData.csv"}, delimiter = '$', lineSeparator = "$")
    void testCsvAnnotationWithUserData(String userData) {
        String[] userDataArray = userData.split("\n");
        HashMap<String, Double> userDataMap = new HashMap<>();
        for (String genreRating : userDataArray) {
            String[] genre = genreRating.split(",");
            userDataMap.put(genre[0].trim(), Double.parseDouble(genre[1].trim()));
        }
        for (Map.Entry<String, Double> genreData : userDataMap.entrySet()) {
            System.out.println(genreData.getValue());
            System.out.println(genreData.getKey());
        }
    }

    @ParameterizedTest
    @CsvFileSource(resources = {"/ArtistGenreSet.csv"}, delimiter = '\n')
    void testCsvReaderAnnotation(String genreLine) {
        String[] genreArray = genreLine.split((", "));
        Set<String> genreSet = new HashSet<>(Set.of(genreArray));


        for (String genre : genreSet) {
            System.out.println(genre);
        }

    }

}
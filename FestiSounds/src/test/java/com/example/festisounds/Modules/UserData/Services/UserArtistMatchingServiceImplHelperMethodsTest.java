package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Core.Utils.GenrePositionMapGenerator;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.example.festisounds.TestUtils.CsvReader.readMapCsv;
import static com.example.festisounds.TestUtils.CsvReader.readSetCsv;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;


class UserArtistMatchingServiceImplHelperMethodsTest {

    UserArtistMatchingServiceImpl userArtistMatchingService = new UserArtistMatchingServiceImpl();
    static GenrePositionMapGenerator genreMapGenerator;
    static HashMap<String, short[]> genrePositionMap;

    // TODO: Test getArtistRanking method
    @BeforeAll
    static void init() {
        genreMapGenerator = new GenrePositionMapGenerator();
        genrePositionMap = genreMapGenerator.makeGenrePositionMap("Genre3DMap.csv");
    }


//    Great finding: the delimiter and line separator may actually not be in the file!
    @SneakyThrows
    @ParameterizedTest
    @CsvFileSource(resources = {"/RobbieGenreData.csv", "/ilianaGenreData.csv", "/maxFirstGenreData.csv"},
            delimiter = '$',
            lineSeparator = "$")
    void testMatchGenreDataToFestivalArtists_WhenGivenInputs_ReturnsHashMap(String userData) {
        String[] userDataArray = userData.split("\n");
        HashMap<String, Double> userDataMap = new HashMap<>();
        for (String genreRating : userDataArray) {
            String[] genre = genreRating.split(",");
            userDataMap.put(genre[0].trim(), Double.parseDouble(genre[1].trim()));
//            System.out.println(genreRating);
        }
        ArtistResponseDTO artist1 = new ArtistResponseDTO(randomUUID(),
                "test1",
                "test1",
                Set.of("edm",
                "house",
                "stutter house"),
                Set.of(randomUUID()));
        ArtistResponseDTO artist2 = new ArtistResponseDTO(randomUUID(),
                "test1",
                "test1",
                Set.of("modern folk rock",
                        "modern rock",
                        "neo mellow",
                        "stomp and holler",
                        "uk americana"),
                Set.of(randomUUID()));


        Set<ArtistResponseDTO> artists = Set.of(artist2, artist1);
        HashMap<ArtistResponseDTO, Double> artistScoresMap =
                userArtistMatchingService.matchGenreDataToFestivalArtists(userDataMap, artists, genrePositionMap);

        for (Map.Entry<ArtistResponseDTO, Double> artistScore : artistScoresMap.entrySet()) {
            System.out.println(artistScore.getKey());
            System.out.println(artistScore.getValue());
        }

        assertEquals(2, artistScoresMap.size(), () -> "not building correct result data structure");
    }


    // TODO: Full test courage of this method (edge cases and exception handling)
    @ParameterizedTest
    @MethodSource("artistScoreParameters")
    void testGetArtistScore_whenProvidedInputs_ReturnsResultInCorrectRange(HashMap<String, Double> genreData,
                                                                            Set<String> artistGenres) {
        double minResult = 0.0;
        double maxResult = 100.0;

        Double actualResult = userArtistMatchingService.getArtistScore(genreData, artistGenres, genrePositionMap);

        System.out.println(actualResult);

        assertNotNull(actualResult);
        assertTrue(actualResult >= minResult && actualResult <= maxResult,
                () -> "Result should be in range " + minResult + "-" + maxResult + " but was " + actualResult);
    }

    @ParameterizedTest
    @MethodSource("genrePositionParameters")
    void testGetDistanceBetweenGenres_whenProvidedInputs_returnsResultInCorrectRange(short[] artistGenrePosition, short[] userGenrePosition) {
        double minResult = 0.0;
        double maxResult = 100.0;

        double actualResult = userArtistMatchingService.getDistanceBetweenGenres(artistGenrePosition, userGenrePosition, genrePositionMap);

        System.out.println(actualResult);

        assertTrue(actualResult >= minResult && actualResult <= maxResult,
                () -> "Result should be in range " + minResult + "-" + maxResult + " but was " + actualResult);
    }

    @ParameterizedTest
    @MethodSource("genrePositionParameters")
    void testCalculateColourDistanceSquared_whenProvidedInputs_returnsResultInCorrectRange(short[] artistGenrePosition, short[] userGenrePosition) {
        double minResult = 0.0;
        double maxResult = 1.0;

        double actualResult = userArtistMatchingService.calculateColourDistanceSquared(artistGenrePosition, userGenrePosition);

        assertTrue(actualResult >= minResult && actualResult <= maxResult,
                () -> "Result should be in range " + minResult + "-" + maxResult + " but was " + actualResult);
    }

    @ParameterizedTest
    @MethodSource("genrePositionParametersWithResult")
    void testCalculateColourDistanceSquared_whenProvidedInputs_returnsCorrectResult(double expectedResult, short[] artistGenrePosition, short[] userGenrePosition) {
        double actualResult = userArtistMatchingService.calculateColourDistanceSquared(artistGenrePosition, userGenrePosition);

        assertEquals(expectedResult, (double) Math.round(100 * actualResult) /100,
                () -> "Expected result: " + expectedResult + " but returned result: " + actualResult);
    }

    public static Stream<Arguments> artistScoreParameters() {
        HashMap<String, Double> maxGenreMap = readMapCsv("maxFirstGenreData.csv");
        HashMap<String, Double> iliGenreMap = readMapCsv("ilianaGenreData.csv");
        HashMap<String, Double> robbieGenreMap = readMapCsv("RobbieGenreData.csv");
        List<HashMap<String, Double>> userMaps = Arrays.asList(maxGenreMap, iliGenreMap, robbieGenreMap);
        ArrayList<Set<String>> genreSets = readSetCsv("ArtistGenreSet.csv");

        return userMaps.stream()
                .flatMap(userMap -> genreSets.stream()
                        .map(genreSet -> Arguments.of(userMap, genreSet)));
    }

    public static Stream<Arguments> genrePositionParametersWithResult() {
        return Stream.of(
                Arguments.of(1.00, new short[]{0, 0, 256, 256, 256}, new short[]{0, 0, 0, 0, 0}),
                Arguments.of(0.00, new short[]{0, 0, 127, 127, 127}, new short[]{0, 0, 127, 127, 127}),
                Arguments.of(0.15, new short[]{0, 0, 100, 100, 100}, new short[]{0, 0, 200, 200, 200})
                );
    }


    public static Stream<Arguments> genrePositionParameters() {
        int counter = 0;
        int numTests = 100;
        short[][] genrePositionArray = new short[numTests+1][];
        for (short[] position : genrePositionMap.values()) {
            if (counter >= numTests) {
                break;
            }
            genrePositionArray[counter] = position;
            counter++;
        }
        return IntStream.range(0, genrePositionArray.length/2)
                .mapToObj(i -> Arguments.of(genrePositionArray[i], genrePositionArray[++i]));
    }

}

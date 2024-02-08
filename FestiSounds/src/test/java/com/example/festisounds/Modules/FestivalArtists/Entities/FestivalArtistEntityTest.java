package com.example.festisounds.Modules.FestivalArtists.Entities;

import com.example.festisounds.Modules.Festival.Entities.Festival;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Set;

@DataJpaTest
public class FestivalArtistEntityTest {
    @Autowired
    private TestEntityManager testManager;

    private FestivalArtist artist1;
    private FestivalArtist artist2;
    private Festival festival;

    @BeforeEach
    void setup() {
        artist1 = new FestivalArtist();
        artist1.setSpotifyId("test1");
        artist1.setArtistName("artist1");
        artist1.setFestivals(Set.of());
        artist1.setGenres(Set.of("techno", "house"));

        artist2 = new FestivalArtist();
        artist2.setFestivals(Set.of());
        artist2.setGenres(Set.of("blues", "jazz"));
    }

    @Test
    @DisplayName("Artist is persisted")
    void testFestivalArtistEntity_whenValidDataIsProvided_persistArtist() {
        FestivalArtist storedArtist = testManager.persistAndFlush(artist1);

        Assertions.assertNotNull(storedArtist.getId());
        Assertions.assertEquals(artist1.getArtistName(), storedArtist.getArtistName(),
                () -> "Artist name does not match");
        Assertions.assertEquals(2, storedArtist.getGenres().size(),
                () -> "Artist should have two genres");
        Assertions.assertEquals(artist1.getSpotifyId(), storedArtist.getSpotifyId(),
                () -> "Spotify ids should match");
    }

    @Test
    @DisplayName("Artist has unique name")
    void testFestivalArtistEntity_whenArtistNameExists_throwsException() {
        FestivalArtist storedArtist = testManager.persistAndFlush(artist1);

        artist2.setArtistName("artist1");
        artist2.setSpotifyId("1");

        Assertions.assertThrows(PersistenceException.class, () -> {
            testManager.persistAndFlush(artist2);
        }, "Artist with existing name should not be saved");
    }

    @Test
    @DisplayName("Artist has unique spotifyId")
    void testFestivalArtistEntity_whenArtistSpotifyIdExists_throwsException() {
        FestivalArtist storedArtist = testManager.persistAndFlush(artist1);

        artist2.setArtistName("The Lumineers");
        artist2.setSpotifyId("test1");

        Assertions.assertThrows(PersistenceException.class, () -> {
            testManager.persistAndFlush(artist2);
        }, "Artist with existing spotifyId should not be saved");
    }

    @Test
    @DisplayName("Festival list must not be null")
    void testFestivalArtistEntity_whenFestivalSetIsNull_throwsException() {
        FestivalArtist testArtist = new FestivalArtist();
        testArtist.setSpotifyId("2");
        testArtist.setArtistName("NoFestival");
        testArtist.setGenres(Set.of("techno", "house"));

        Assertions.assertThrows(PersistenceException.class, () -> {
            testManager.persistAndFlush(testArtist);
        }, "Festival set should always exist");
    }

    @Test
    @DisplayName("Artist should have genres")
    void testFestivalArtistEntity_whenNoGenreIsPresent_throwsException() {
        FestivalArtist testArtist = new FestivalArtist();
        testArtist.setSpotifyId("2");
        testArtist.setArtistName("NoFestival");
        testArtist.setFestivals(Set.of());

        Assertions.assertThrows(PersistenceException.class, () -> {
            testManager.persistAndFlush(testArtist);
        }, "Genres should always exist");
    }


}

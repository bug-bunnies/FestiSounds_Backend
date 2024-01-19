package com.example.festisounds.Modules.FestivalArtists.Service;

import com.example.festisounds.Core.Controllers.SpotifyClientCredentials;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.Festival.Service.FestivalDTOBuilder;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;
import com.example.festisounds.Modules.FestivalArtists.Repositories.FestivalArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.example.festisounds.Core.Controllers.AuthController.spotifyApi;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final FestivalArtistRepository repository;
    private final FestivalRepo festivalRepo;

    public Artist[] getSpotifyArtistData(String name) {
        SpotifyClientCredentials.checkForToken();

        SearchArtistsRequest searchArtist = spotifyApi.searchArtists(name)
               .build();

        try {
            Artist[] artist = searchArtist.execute().getItems();
            return artist;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong getting top tracks!\n" + e.getMessage());
        }
    }

    public ArtistDTO createArtist(String name, UUID festivalId) throws SQLException {
        try {
            name = name.toUpperCase().strip();
            FestivalArtist artist = findArtist(name);
            if (artist != null) {
                throw new SQLException("artist already exist!");
            }

            Festival festival = festivalRepo.findById(festivalId).orElseThrow();
            Artist[] art = getSpotifyArtistData(name);

            String spotyId = Arrays
                   .stream(art)
                   .map(x -> x.getId())
                   .findFirst()
                   .orElse("not there");

            String[] genres = Arrays
                   .stream(art)
                   .map(x -> x.getGenres())
                   .findFirst()
                   .orElse(new String[]{"no!"});

            FestivalArtist newArtist = repository.save(new FestivalArtist(spotyId, name, festival, genres));
            return FestivalDTOBuilder.artistDataBuilder(newArtist);
        } catch (SQLException e) {
            addArtistToFestival(name, festivalId);
        }
        return null;
    }

    public ArtistDTO getArtist(UUID artistId) {
        FestivalArtist artist = repository.findById(artistId).orElseThrow();
        ArtistDTO result = new ArtistDTO(artistId, artist.getSpotifyId(),
                artist.getArtistName(),
                artist.getGenres(),
                artist.getFestivals().stream()
                        .map(f -> f.getName())
                        .collect(Collectors.toSet()));
        return result;
    }

    public FestivalArtist findArtist(String name) {
        name = name.toUpperCase().strip();
        FestivalArtist artist = repository.findFestivalArtistByArtistName(name);
        return artist;
    }

    public String addArtistToFestival(String name, UUID festivalId) {
        Festival festival = festivalRepo.findById(festivalId).orElseThrow();
        FestivalArtist artist = findArtist(name);
        artist.getFestivals().add(festival);
        repository.save(artist);
        return festival.getName();
    }

    public Set<String> updateArtistGenres(String name) throws Exception {
        try {
            FestivalArtist existingArtist = findArtist(name);

            Artist[] spotifyArtistData = getSpotifyArtistData(name);

            String[] genres = Arrays
                    .stream(spotifyArtistData)
                    .peek(System.out::println)
                    .map(x -> x.getGenres())
                    .findFirst()
                    .orElse(new String[]{"no!"});

            for (String genre : genres) {
                existingArtist.getGenres().add(genre);
                System.out.println(genre + " here is genre in loop");
            }

            repository.save(existingArtist);
            return existingArtist.getGenres();

        } catch (Exception e) {
            throw new Exception("Artist not found!");
        }
    }

    public void deleteArtist(UUID id) {
        repository.deleteById(id);
    }

}

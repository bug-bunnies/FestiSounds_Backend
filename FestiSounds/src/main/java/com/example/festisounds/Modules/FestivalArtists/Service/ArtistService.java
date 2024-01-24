package com.example.festisounds.Modules.FestivalArtists.Service;

import com.example.festisounds.Core.Controllers.SpotifyClientCredentials;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.Festival.Service.FestivalDTOBuilder;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;
import com.example.festisounds.Modules.FestivalArtists.Repositories.FestivalArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final FestivalArtistRepository artistRepository;
    private final FestivalRepo festivalRepo;

    private final SpotifyClientCredentials spotify;


    public Artist[] getSpotifyArtistData(String name) {
        SpotifyApi spotifyApi = SpotifyClientCredentials.checkForToken();

        SearchArtistsRequest searchArtist = spotifyApi.searchArtists(name)
                .build();

        try {
            return searchArtist.execute().getItems();
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong getting top tracks!\n" + e.getMessage());
        }
    }

    public ArtistResponseDTO createArtist(String name, Festival festival) {
        Artist[] artist = getSpotifyArtistData(name);

        String spotifyId = Arrays
                .stream(artist)
                .map(Artist::getId)
                .findFirst()
                .orElse("Could not find a spotifyId for the Artist");

        String[] genres = Arrays
                .stream(artist)
                .map(Artist::getGenres)
                .findFirst()
                .orElse(new String[]{"Could not find a genres for the Artist"});

        FestivalArtist newArtist = artistRepository.save(new FestivalArtist(spotifyId, name, festival, genres));
        return FestivalDTOBuilder.artistDTOBuilder(newArtist);
}

    public ArtistResponseDTO createOrAddArtistRouter(String name, UUID festivalId) {
        Festival festival = festivalRepo.findById(festivalId).orElseThrow();
        name = name.toUpperCase().strip();   // may cause issues if artist name is case-sensitive
        FestivalArtist artist = findArtist(name);
        if (artist == null) {
            return createArtist(name, festival);
        }
        return addArtistToFestival(artist, festival);
    }

    public ArtistResponseDTO getArtist(UUID artistId) {
        FestivalArtist artist = artistRepository.findById(artistId).orElseThrow();
        return FestivalDTOBuilder.artistDTOBuilder(artist);
    }
    public FestivalArtist findArtist(String name) {
        name = name.toUpperCase().strip();
        return artistRepository.findFestivalArtistByArtistName(name);
    }

    public ArtistResponseDTO addArtistToFestival(FestivalArtist artist, Festival festival) {
            artist.getFestivals().add(festival);
            return FestivalDTOBuilder.artistDTOBuilder(artistRepository.save(artist));
    }

    public Set<String> updateArtistGenres(String name) throws Exception {
        try {
            FestivalArtist existingArtist = findArtist(name);

            Artist[] spotifyArtistData = getSpotifyArtistData(name);

            String[] genres = Arrays
                    .stream(spotifyArtistData)
                    .peek(System.out::println)
                    .map(Artist::getGenres)
                    .findFirst()
                    .orElse(new String[]{"no!"});

            for (String genre : genres) {
                existingArtist.getGenres().add(genre);
                System.out.println(genre + " here is genre in loop");
            }

            artistRepository.save(existingArtist);
            return existingArtist.getGenres();

        } catch (Exception e) {
            throw new Exception("Artist not found!");
        }
    }

    public void deleteArtist(UUID id) {
        artistRepository.deleteById(id);
    }

}

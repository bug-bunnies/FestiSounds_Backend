package com.example.festisounds.Modules.FestivalArtists.Service;

import com.example.festisounds.Core.Controllers.SpotifyClientCredentials;
import com.example.festisounds.Core.Exceptions.Festival.FestivalNotFoundException;
import com.example.festisounds.Core.Exceptions.FestivalArtists.ArtistNotFoundException;
import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.Festival.Service.FestivalDTOBuilder;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistRequestDTO;
import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistResponseDTO;
import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;
import com.example.festisounds.Modules.FestivalArtists.Repositories.FestivalArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final FestivalArtistRepository artistRepository;
    private final FestivalRepo festivalRepo;

//    TODO: check client credential functionality/structure
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

// TODO: test if filtering works as expected and disable test before pushing
    public ArtistRequestDTO findArtistInSpotifyAndCreateArtistObject(String name, Artist[] artist) {
        String spotifyId = Arrays
                .stream(artist)
                .map(Artist::getId)
                .findFirst()
                .orElseThrow(() -> new ArtistNotFoundException("Could not find a spotifyId for the Artist"));

        String[] genres = Arrays
                .stream(artist)
                .map(Artist::getGenres)
                .findFirst()
                .orElseThrow(() -> new ArtistNotFoundException("Could not find a genre for the Artist"));

        return new ArtistRequestDTO(spotifyId, name, Set.of(genres));
    }

    public ArtistResponseDTO getArtist(UUID artistId) throws ArtistNotFoundException {
        FestivalArtist artist = artistRepository
                .findById(artistId)
                .orElseThrow(() -> new ArtistNotFoundException("Could not find artist with id " + artistId));
        return FestivalDTOBuilder.artistDTOBuilder(artist);
    }

    public ArtistResponseDTO createOrAddArtistRouter(String name, UUID festivalId) {
        Festival festival = festivalRepo
                .findById(festivalId)
                .orElseThrow(() -> new FestivalNotFoundException("Could not find a festival by the id: " + festivalId));

        name = name.toUpperCase().strip();   // may cause issues if artist name is case-sensitive
        FestivalArtist artist = findArtist(name);

        if (artist == null) {
            Artist[] spotifyArtist = getSpotifyArtistData(name);
            ArtistRequestDTO request = findArtistInSpotifyAndCreateArtistObject(name, spotifyArtist);
            return createArtist(festival, request);
        }

        return addArtistToFestival(artist, festival);
    }

//    TODO: if returned value is null, should we handle it here?
    public FestivalArtist findArtist(String name) {
        name = name.toUpperCase().strip();
        return artistRepository.findFestivalArtistByArtistName(name);
    }

    public ArtistResponseDTO createArtist(Festival festival, ArtistRequestDTO newArtist) {
        FestivalArtist createdArtist = new FestivalArtist(newArtist.spotifyId(),
                newArtist.artistName(),
                festival,
                newArtist.genres().toArray(new String[0]));

        createdArtist = artistRepository.save(createdArtist);
        return FestivalDTOBuilder.artistDTOBuilder(createdArtist);
    }


    public ArtistResponseDTO addArtistToFestival(FestivalArtist artist, Festival festival) {
        artist.getFestivals().add(festival);
        return FestivalDTOBuilder.artistDTOBuilder(artistRepository.save(artist));
    }

//    TODO: test after spotify data has been tested
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

    public void deleteArtist(UUID id) throws ArtistNotFoundException {
        if (artistRepository.findById(id).isPresent()) {
            artistRepository.deleteById(id);
        } else {
            throw new ArtistNotFoundException("Artist with id  " + id + " does not exist");
        }

    }

}

package com.example.festisounds.Modules.FestivalArtists.Service;

import com.example.festisounds.Modules.Festival.Entities.Festival;
import com.example.festisounds.Modules.Festival.Repository.FestivalRepo;
import com.example.festisounds.Modules.FestivalArtists.Entities.FestivalArtist;
import com.example.festisounds.Modules.FestivalArtists.Repositories.FestivalArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import se.michaelthelin.spotify.requests.data.search.simplified.SearchArtistsRequest;

import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final FestivalArtistRepository repository;
    private final FestivalRepo festivalRepo;

    private static String token =

                   "BQCXld07JUNJYWMSjtKXaXtzOQcgWJR8nZsEdrao0Mi6K5o9s_2OpAEauxp7R0q5p5otejk5hvOBfdH5gF_IpRgBA-JYn02235q5TMcJu__E3Fg0ohtIEzaMUEP_RJjAtQINNCtMLiOcaTPm0iOf4SbkqTUOXe1j9UOmL0tppd5ZPIaGSJPmE-gxTgM013deOytjgI1LxxLXUS3qPrrYhikEYpQ";

    private static final String clientId = System.getenv("clientId");
    private static final String clientSecret = System.getenv("clientSecret");

    public static final SpotifyApi spotify = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setAccessToken(token)
            .build();


    public FestivalArtist createArtist(String name, UUID festivalId) {
        Festival festival = festivalRepo.findById(festivalId).orElseThrow();
       Artist[] art = getSpotifyId(name);
       String spotyId = Arrays.stream(art).map(x -> x.getId()).findFirst().orElse("not there");
       String[] genres = Arrays
               .stream(art)
               .map(x -> x.getGenres())
               .findFirst()
               .orElse(new String[]{"no!"});

       name = name.toUpperCase().strip();
       FestivalArtist newArtist = repository.save(new FestivalArtist(spotyId, name, festival, genres));
       return newArtist;
    }



    public Artist[] getSpotifyId(String name) {
       SearchArtistsRequest searchArtist = spotify.searchArtists(name)
               .build();

        try {
            Artist[] artist = searchArtist.execute().getItems();
            return artist;
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong getting top tracks!\n" + e.getMessage());
        }

    }
}

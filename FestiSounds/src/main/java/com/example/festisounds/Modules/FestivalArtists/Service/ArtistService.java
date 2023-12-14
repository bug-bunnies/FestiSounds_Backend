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
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class ArtistService {
    private final FestivalArtistRepository repository;
    private final FestivalRepo festivalRepo;

    private static String token =
            "BQBhClwu5_-dVMFidjgeiBlBjtfs1D7tTHLa_eKh1ZsdQp3hXUEnMd83w23JrqVLtuIbFLdWzg3lyBpi3saIB6dYhioYGrwcBuP2b12M8-AFKuwNskfbSPYdRTNjJExpxEd7xK3VvQwMrefMJ9EmYnrZzm7qYyWVppq0g7nSgSFg7d7Mo5Jl3wxfL8BGHlSnEifvbq21cFUtY5cOJPBiHyVwnvk";

    private static final String clientId = System.getenv("clientId");
    private static final String clientSecret = System.getenv("clientSecret");

    public static final SpotifyApi spotify = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setAccessToken(token)
            .build();


    public FestivalArtist createArtist(String name, UUID festivalId) {
        Festival festival = festivalRepo.findById(festivalId).orElseThrow();
       String art = getSpotifyId(name);
       name = name.toUpperCase().strip();
       FestivalArtist newArtist = repository.save(new FestivalArtist(art, name));
       newArtist.getFestivals().add(festival);
       return newArtist;
    }

    public String getSpotifyId(String name) {
       SearchArtistsRequest searchArtist = spotify.searchArtists(name)
               .build();

        try {
            Artist[] artist = searchArtist.execute().getItems();
            return Arrays.stream(artist).map(x -> x.getId()).findFirst().orElse("not there");
        } catch (Exception e) {
            throw new RuntimeException("Something went wrong getting top tracks!\n" + e.getMessage());
        }

    }
}

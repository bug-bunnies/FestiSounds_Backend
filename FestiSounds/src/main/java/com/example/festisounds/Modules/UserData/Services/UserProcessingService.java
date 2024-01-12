package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.FestivalArtists.DTO.ArtistDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopArtistsDTO;
import com.example.festisounds.Modules.UserData.DTOs.TopTracksDTO;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.Artist;
import org.apache.hc.core5.http.ParseException;


import java.io.IOException;
import java.util.HashMap;

public interface UserProcessingService {
    HashMap<ArtistDTO, Double> getArtistRankingFromFestival(String festivalId) throws IOException, ParseException, SpotifyWebApiException;;

    HashMap<String, Double> rankUsersFavouriteGenres() throws IOException, ParseException, SpotifyWebApiException;
    HashMap<String, Double> getGenreRankingFromArtists(TopArtistsDTO topArtistsDTO);
    HashMap<String, Double> generateGenreRanking(Artist[] artists);
    HashMap<String, Double> getGenreRankingFromTracks(TopTracksDTO topTracksDTO);

}

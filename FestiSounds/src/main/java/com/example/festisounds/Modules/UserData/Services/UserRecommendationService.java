package com.example.festisounds.Modules.UserData.Services;

import com.example.festisounds.Modules.UserData.DTOs.RecommendedArtistsDTO;
import org.apache.hc.core5.http.ParseException;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;

import java.io.IOException;
import java.util.UUID;

public interface UserRecommendationService {


    RecommendedArtistsDTO recommendArtists(UUID festivalId) throws IOException, ParseException, SpotifyWebApiException;
}

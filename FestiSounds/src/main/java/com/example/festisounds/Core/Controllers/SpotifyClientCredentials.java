package com.example.festisounds.Core.Controllers;

import org.springframework.stereotype.Service;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.credentials.ClientCredentials;
import se.michaelthelin.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;

import java.io.IOException;
import org.apache.hc.core5.http.ParseException;

@Service
public class SpotifyClientCredentials {
    private static final String clientId = System.getenv("clientId");
    private static final String clientSecret = System.getenv("clientSecret");

    private static final SpotifyApi spotifyClientApi = new SpotifyApi.Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .build();
    private static final ClientCredentialsRequest clientCredentialsRequest = spotifyClientApi.clientCredentials()
            .build();


    public static SpotifyApi clientCredentials_Sync() {
        try {
            final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

            // Set access token for further "spotifyApi" object usage
            spotifyClientApi.setAccessToken(clientCredentials.getAccessToken());

            System.out.println("Expires in: " + clientCredentials.getExpiresIn() + clientCredentials.getAccessToken());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            System.out.println("Error: " + e.getMessage());
        }
            return spotifyClientApi;
    }

    public static SpotifyApi checkForToken() {
            String token = spotifyClientApi.getAccessToken();
            if (token == null) {
                clientCredentials_Sync();
            } else {
                System.out.println(token);
            }
        return spotifyClientApi;
    }


//    Async alternative:::
//    public static void clientCredentials_Async() {
//        try {
//            final CompletableFuture<ClientCredentials> clientCredentialsFuture = clientCredentialsRequest.executeAsync();
//
//            // Thread free to do other tasks...
//
//            // Example Only. Never block in production code.
//            final ClientCredentials clientCredentials = clientCredentialsFuture.join();
//
//            // Set access token for further "spotifyApi" object usage
//            spotifyApi.setAccessToken(clientCredentials.getAccessToken());
//
//            System.out.println("Expires in: " + clientCredentials.getExpiresIn());
//        } catch (CompletionException e) {
//            System.out.println("Error: " + e.getCause().getMessage());
//        } catch (CancellationException e) {
//            System.out.println("Async operation cancelled.");
//        }
//    }
}

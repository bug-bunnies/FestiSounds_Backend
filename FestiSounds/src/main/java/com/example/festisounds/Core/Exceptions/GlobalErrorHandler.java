package com.example.festisounds.Core.Exceptions;

import com.example.festisounds.Core.Controllers.AuthController;
import com.example.festisounds.Core.Exceptions.Festival.FestivalNotFoundException;
import com.example.festisounds.Core.Exceptions.FestivalArtists.ArtistNotFoundException;
import org.apache.hc.core5.http.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.exceptions.detailed.UnauthorizedException;

import java.io.IOException;

@ControllerAdvice
public class GlobalErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    protected ResponseEntity<String> handleError(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }

    @ExceptionHandler({SpotifyWebApiException.class})
    protected void handleSpotifyUnauthorized(SpotifyWebApiException ex) throws IOException, ParseException, SpotifyWebApiException {
        if (ex instanceof UnauthorizedException) {
            AuthController.refreshAccessToken();
        }
    }

    @ExceptionHandler({FestivalNotFoundException.class})
    public ResponseEntity<Object> handleFestivalNotFoundException(FestivalNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler({ArtistNotFoundException.class})
    public ResponseEntity<Object> handleArtistNotFoundException(ArtistNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }
}
package com.example.festisounds.Core.Controllers;

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
            String errorMessage = ex.getMessage();

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(errorMessage);
        }

    @ExceptionHandler({SpotifyWebApiException.class})
    protected void handleSpotifyUnautherized(SpotifyWebApiException ex) throws IOException, ParseException, SpotifyWebApiException {
        if (ex instanceof UnauthorizedException) {
            AuthController.refreshAccessToken();
        }
    }
}
package com.foursquare.handler;

import com.foursquare.dto.ErrorResponseDto;
import com.foursquare.exception.FourSquareApiException;
import com.foursquare.exception.UserExistException;
import com.foursquare.exception.VenueException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ErrorResponseExceptionHandler {

    @ExceptionHandler(FourSquareApiException.class)
    public final ErrorResponseDto handleFourSquareApiException(FourSquareApiException ex) {
        return new ErrorResponseDto(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(UserExistException.class)
    public final ErrorResponseDto handleUserExistException(UserExistException ex) {
        return new ErrorResponseDto(409, ex.getMessage());
    }

    @ExceptionHandler(VenueException.class)
    public final ErrorResponseDto handleVenueException(VenueException ex) {
        return new ErrorResponseDto(409, ex.getMessage());
    }
}
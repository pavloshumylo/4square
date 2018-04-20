package com.foursquare.handler;

import com.foursquare.dto.ErrorResponseDto;
import com.foursquare.exception.FourSquareApiException;
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
}
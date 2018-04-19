package com.foursquare.exception.handler;

import com.foursquare.dto.ErrorResponseDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
@RestController
public class ErrorResponseExceptionHandler {

    @ExceptionHandler(HttpClientErrorException.class)
    public final ErrorResponseDto handleHttpClientErrorException(HttpClientErrorException ex) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto();
        errorResponseDto.setCode(ex.getStatusCode().toString());
        errorResponseDto.setMessage(ex.getMessage());
        return errorResponseDto;
    }
}
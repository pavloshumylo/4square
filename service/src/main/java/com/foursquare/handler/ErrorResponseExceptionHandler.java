package com.foursquare.handler;

import com.foursquare.dto.ErrorResponseDto;
import com.foursquare.exception.FourSquareApiException;
import com.foursquare.exception.ResourceNotFoundException;
import com.foursquare.exception.UserExistException;
import com.foursquare.exception.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @ExceptionHandler(BadRequestException.class)
    public final ErrorResponseDto handleBadRequestException(BadRequestException ex) {
        return new ErrorResponseDto(400, ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
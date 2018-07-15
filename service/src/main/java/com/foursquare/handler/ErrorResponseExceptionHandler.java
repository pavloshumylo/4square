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

@RestController
@ControllerAdvice
public class ErrorResponseExceptionHandler {

    @ExceptionHandler(FourSquareApiException.class)
    public final ResponseEntity<ErrorResponseDto> handleFourSquareApiException(FourSquareApiException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(ex.getCode(), ex.getMessage()), HttpStatus.valueOf(ex.getCode()));
    }

    @ExceptionHandler(UserExistException.class)
    public final ResponseEntity<ErrorResponseDto> handleUserExistException(UserExistException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(409, ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorResponseDto> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(400, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ErrorResponseDto> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(new ErrorResponseDto(404, ex.getMessage()), HttpStatus.NOT_FOUND);
    }
}
package com.foursquare.handler;

import com.foursquare.dto.ErrorResponseDto;
import com.foursquare.exception.BadRequestException;
import com.foursquare.exception.FourSquareApiException;
import com.foursquare.exception.ResourceNotFoundException;
import com.foursquare.exception.UserExistException;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ErrorResponseExceptionHandlerTest {

    private ResponseEntity responseEntityExpected, responseEntityActual;

    @Test
    public void testHandleFourSquareApiException_ShouldReturnErrorResponseDto() {
        responseEntityExpected = new ResponseEntity<>(new ErrorResponseDto(200, "TestMessage"), HttpStatus.OK);
        responseEntityActual = new ErrorResponseExceptionHandler().
                handleFourSquareApiException(new FourSquareApiException(200, "TestMessage"));

        assertEquals(responseEntityExpected, responseEntityActual);
    }

    @Test
    public void testHandleUserExistException_ShouldReturnErrorResponseDto() {
        responseEntityExpected = new ResponseEntity<>(new ErrorResponseDto(409, "TestMessage"), HttpStatus.CONFLICT);
        responseEntityActual = new ErrorResponseExceptionHandler().
                handleUserExistException(new UserExistException("TestMessage"));

        assertEquals(responseEntityExpected, responseEntityActual);
    }

    @Test
    public void testHandleBadRequestException_ShouldReturnErrorResponseDto() {
        responseEntityExpected = new ResponseEntity<>(new ErrorResponseDto(400, "TestMessage"), HttpStatus.BAD_REQUEST);
        responseEntityActual = new ErrorResponseExceptionHandler().
                handleBadRequestException(new BadRequestException("TestMessage"));

        assertEquals(responseEntityExpected, responseEntityActual);
    }

    @Test
    public void testHandleResourceNotFoundException_ShouldReturnNotFoundResponseEntityWithBody() {
        responseEntityExpected = new ResponseEntity<>(new ErrorResponseDto(404, "TestMessage"), HttpStatus.NOT_FOUND);
        responseEntityActual = new ErrorResponseExceptionHandler().
                handleResourceNotFoundException(new ResourceNotFoundException("TestMessage"));

        assertEquals(responseEntityExpected, responseEntityActual);
    }
}

package com.foursquare.handler;

import com.foursquare.dto.ErrorResponseDto;
import com.foursquare.exception.FourSquareApiException;
import com.foursquare.exception.ResourceNotFoundException;
import com.foursquare.exception.UserExistException;
import com.foursquare.exception.BadRequestException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class ErrorResponseExceptionHandlerTest {

    private ErrorResponseDto errorResponseDtoExpected, errorResponseDtoActual;
    private ErrorResponseExceptionHandler errorResponseExceptionHandler;

    @Before
    public void init() {
        errorResponseExceptionHandler = new ErrorResponseExceptionHandler();
    }


    @Test
    public void testHandleFourSquareApiException_ShouldReturnErrorResponseDto() {
        errorResponseDtoExpected = new ErrorResponseDto(200, "TestMessage");
        errorResponseDtoActual = errorResponseExceptionHandler.
                handleFourSquareApiException(new FourSquareApiException(200, "TestMessage"));

        assertEquals(errorResponseDtoExpected, errorResponseDtoActual);
    }

    @Test
    public void testHandleUserExistException_ShouldReturnErrorResponseDto() {
        errorResponseDtoExpected = new ErrorResponseDto(409, "TestMessage");
        errorResponseDtoActual = errorResponseExceptionHandler.
                handleUserExistException(new UserExistException("TestMessage"));

        assertEquals(errorResponseDtoExpected, errorResponseDtoActual);
    }

    @Test
    public void testHandleBadRequestException_ShouldReturnErrorResponseDto() {
        errorResponseDtoExpected = new ErrorResponseDto(400, "TestMessage");
        errorResponseDtoActual = errorResponseExceptionHandler.
                handleBadRequestException(new BadRequestException("TestMessage"));

        assertEquals(errorResponseDtoExpected, errorResponseDtoActual);
    }

    @Test
    public void testHandleResourceNotFoundException_ShouldReturnNotFoundResponseEntityWithBody() {
        ResponseEntity<String> responseEntityExpected = new ResponseEntity<>("TestMessage", HttpStatus.NOT_FOUND);
        ResponseEntity responseEntityActual = errorResponseExceptionHandler
                .handleResourceNotFoundException(new ResourceNotFoundException("TestMessage"));

        assertEquals(responseEntityExpected, responseEntityActual);
    }
}

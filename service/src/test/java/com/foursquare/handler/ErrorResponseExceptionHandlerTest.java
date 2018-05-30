package com.foursquare.handler;

import com.foursquare.dto.ErrorResponseDto;
import com.foursquare.exception.FourSquareApiException;
import com.foursquare.exception.UserExistException;
import com.foursquare.exception.VenueException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorResponseExceptionHandlerTest {

    private ErrorResponseDto errorResponseDtoExpected, errorResponseDtoActual;

    @Test
    public void testHandleFourSquareApiException_ShouldReturnErrorResponseDto() {
        errorResponseDtoExpected = new ErrorResponseDto(200, "TestMessage");
        errorResponseDtoActual = new ErrorResponseExceptionHandler().
                handleFourSquareApiException(new FourSquareApiException(200, "TestMessage"));

        assertEquals(errorResponseDtoExpected, errorResponseDtoActual);
    }

    @Test
    public void testHandleUserExistException_ShouldReturnErrorResponseDto() {
        errorResponseDtoExpected = new ErrorResponseDto(409, "TestMessage");
        errorResponseDtoActual = new ErrorResponseExceptionHandler().
                handleUserExistException(new UserExistException("TestMessage"));

        assertEquals(errorResponseDtoExpected, errorResponseDtoActual);
    }

    @Test
    public void testHandleVenueException_ShouldReturnErrorResponseDto() {
        errorResponseDtoExpected = new ErrorResponseDto(409, "TestMessage");
        errorResponseDtoActual = new ErrorResponseExceptionHandler().
                handleVenueException(new VenueException("TestMessage"));

        assertEquals(errorResponseDtoExpected, errorResponseDtoActual);
    }
}

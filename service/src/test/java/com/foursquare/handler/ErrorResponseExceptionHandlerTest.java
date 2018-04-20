package com.foursquare.handler;

import com.foursquare.dto.ErrorResponseDto;
import com.foursquare.exception.FourSquareApiException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorResponseExceptionHandlerTest {

    private ErrorResponseExceptionHandler errorResponseExceptionHandler;
    private FourSquareApiException fourSquareApiException;
    private ErrorResponseDto errorResponseDtoExpected;

    @Before
    public void init() {
        errorResponseExceptionHandler = new ErrorResponseExceptionHandler();
        fourSquareApiException = new FourSquareApiException();
        fourSquareApiException.setMessage("TestMessage");
        fourSquareApiException.setCode(200);
        errorResponseDtoExpected = new ErrorResponseDto(200, "TestMessage");
    }

    @Test
    public void testHandleFourSquareApiException_ShouldReturnErrorResponseDto() {
        ErrorResponseDto errorResponseDtoActual = errorResponseExceptionHandler.
                handleFourSquareApiException(fourSquareApiException);
        assertEquals(errorResponseDtoExpected, errorResponseDtoActual);
    }
}

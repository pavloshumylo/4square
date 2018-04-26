package com.foursquare.handler;

import com.foursquare.dto.ErrorResponseDto;
import com.foursquare.exception.FourSquareApiException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorResponseExceptionHandlerTest {

    @Test
    public void testHandleFourSquareApiException_ShouldReturnErrorResponseDto() {
        ErrorResponseDto errorResponseDtoExpected = new ErrorResponseDto(200, "TestMessage");
        ErrorResponseDto errorResponseDtoActual = new ErrorResponseExceptionHandler().
                handleFourSquareApiException(new FourSquareApiException(200, "TestMessage"));

        assertEquals(errorResponseDtoExpected, errorResponseDtoActual);
    }
}

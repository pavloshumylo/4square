package com.foursquare.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.exception.FourSquareApiException;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.mock.http.client.MockClientHttpResponse;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ResponseErrorHandlerImplTest {

    @Test
    public void testHandleError_ShouldThrowFourSquareApiException() throws IOException {
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            InputStream is = classLoader.getResourceAsStream("testData/response_error_handler_with_400_status.json");
            JsonNode jsonFromDao = new ObjectMapper().readTree(is);

            new ResponseErrorHandlerImpl().handleError(new MockClientHttpResponse(
                    new ObjectMapper().writeValueAsBytes(jsonFromDao), BAD_REQUEST));

            Assert.fail();
        } catch (FourSquareApiException ex) {
            assertTrue(ex instanceof FourSquareApiException);
            assertEquals(400, ex.getCode());
            assertEquals("Couldn't geocode param near: lvive", ex.getMessage());
        }
    }
}
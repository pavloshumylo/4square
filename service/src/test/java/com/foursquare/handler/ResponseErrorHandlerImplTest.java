package com.foursquare.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.exception.FourSquareApiException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ResponseErrorHandlerImplTest {

    @Autowired
    private ObjectMapper objectMapper;

    private ResponseErrorHandlerImpl responseErrorHandler;
    private JsonNode jsonFromDao;


    @Before
    public void init() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/response_error_handler_with_400_status.json");
        jsonFromDao = new ObjectMapper().readValue(is, JsonNode.class);
        responseErrorHandler = new ResponseErrorHandlerImpl();
    }

    @Test
    public void testHandleError_ShouldThrowFourSquareApiException() throws JsonProcessingException {
        try {
            responseErrorHandler.
                    handleError(new MockClientHttpResponse(objectMapper.writeValueAsBytes(jsonFromDao), BAD_REQUEST));
            Assert.fail();
        } catch (FourSquareApiException ex) {
            assertTrue(ex instanceof FourSquareApiException);
            assertEquals(400, ex.getCode());
            assertEquals("Couldn't geocode param near: lvive", ex.getMessage());
        }
    }
}
package com.foursquare.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonResponseValidatorTest {

    @Test
    public void testIsValidVenue_ShouldReturnTrue() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("expectedMockDaoResponse.json");
        JsonNode jsonNodes = new ObjectMapper().readValue(is, JsonNode.class);
        JsonNode jsonVenue = jsonNodes.get("testJsons").get(0).get("response").get("venues").get(0);

        boolean actualValidatorResult = JsonResponseValidator.isValidVenue(jsonVenue);
        assertEquals(true, actualValidatorResult);
    }

    @Test
    public void testIsValidVenue_ShouldReturnFalse() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("expectedMockDaoResponse.json");
        JsonNode jsonNodes = new ObjectMapper().readValue(is, JsonNode.class);
        JsonNode jsonVenue = jsonNodes.get("testJsons").get(2).get("response").get("venues").get(0);

        boolean actualValidatorResult = JsonResponseValidator.isValidVenue(jsonVenue);
        assertEquals(false, actualValidatorResult);
    }

    @Test
    public void testIsValidNode_ShouldReturnTrue() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("expectedMockDaoResponse.json");
        JsonNode jsonNodes = new ObjectMapper().readValue(is, JsonNode.class);
        JsonNode jsonNode = jsonNodes.get("testJsons").get(0)
                .get("response").get("venues").get(0).get("contact").get("phone");

        boolean actualValidatorResult = JsonResponseValidator.isValidNode(jsonNode);
        assertEquals(true, actualValidatorResult);
    }

    @Test
    public void testIsValidNode_ShouldReturnFalse() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("expectedMockDaoResponse.json");
        JsonNode jsonNodes = new ObjectMapper().readValue(is, JsonNode.class);
        JsonNode jsonNode = jsonNodes.get("testJsons").get(3)
                .get("response").get("venues").get(0).get("location").get("address");

        boolean actualValidatorResult = JsonResponseValidator.isValidNode(jsonNode);
        assertEquals(false, actualValidatorResult);
    }

    @Test
    public void testIsValidVenue_ShouldThrowNullPointerException() {
        try {
            JsonResponseValidator.isValidVenue(null);
            Assert.fail();
        } catch (Exception ex) {
            assertTrue(ex instanceof NullPointerException);
        }
    }
}

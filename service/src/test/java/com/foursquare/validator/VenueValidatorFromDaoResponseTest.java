package com.foursquare.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class VenueValidatorFromDaoResponseTest {

    @Test
    public void testIsValidVenue_ShouldReturnTrue() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/venue_validator_from_dao_response_valid_venue.json");
        JsonNode jsonVenue = new ObjectMapper().readValue(is, JsonNode.class);

        boolean actualValidatorResult = VenueValidatorFromDaoResponse.isValidVenue(jsonVenue.get("venue"));
        assertEquals(true, actualValidatorResult);
    }

    @Test
    public void testIsValidVenue_ShouldReturnFalse() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/venue_validator_from_dao_response_venue_without_id.json");
        JsonNode jsonVenue = new ObjectMapper().readValue(is, JsonNode.class);

        boolean actualValidatorResult = VenueValidatorFromDaoResponse.isValidVenue(jsonVenue.get("venue"));
        assertEquals(false, actualValidatorResult);
    }
}

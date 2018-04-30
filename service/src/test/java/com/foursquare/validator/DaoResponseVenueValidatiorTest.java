package com.foursquare.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class DaoResponseVenueValidatiorTest {

    @Test
    public void testIsValidVenue_ShouldReturnTrue() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/venue_validator_from_dao_response_valid_venue.json");
        JsonNode jsonVenue = new ObjectMapper().readTree(is);

        boolean actualValidatorResult = DaoResponseVenueValidatior.isValidVenue(jsonVenue.get("venue"));
        assertEquals(true, actualValidatorResult);
    }

    @Test
    public void testIsValidVenue_venueWithoutId_ShouldReturnFalse() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/venue_validator_from_dao_response_venue_without_id.json");
        JsonNode jsonVenue = new ObjectMapper().readTree(is);

        boolean actualValidatorResult = DaoResponseVenueValidatior.isValidVenue(jsonVenue.get("venue"));
        assertEquals(false, actualValidatorResult);
    }

    @Test
    public void testIsValidVenue_venueWithoutAddress_ShouldReturnTrue() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/venue_validator_from_dao_response_venue_without_address.json");
        JsonNode jsonVenue = new ObjectMapper().readTree(is);

        boolean actualValidatorResult = DaoResponseVenueValidatior.isValidVenue(jsonVenue.get("venue"));
        assertEquals(true, actualValidatorResult);
    }

    @Test
    public void testIsValidVenue_venueWithoutName_ShouldReturnTrue() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/venue_validator_from_dao_response_venue_without_name.json");
        JsonNode jsonVenue = new ObjectMapper().readTree(is);

        boolean actualValidatorResult = DaoResponseVenueValidatior.isValidVenue(jsonVenue.get("venue"));
        assertEquals(true, actualValidatorResult);
    }

    @Test
    public void testIsValidVenue_venueEmpty_ShouldReturnTrue() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/venue_validator_from_dao_response_empty_venue.json");
        JsonNode jsonVenue = new ObjectMapper().readTree(is);

        boolean actualValidatorResult = DaoResponseVenueValidatior.isValidVenue(jsonVenue.get("venue"));
        assertEquals(false, actualValidatorResult);
    }
}
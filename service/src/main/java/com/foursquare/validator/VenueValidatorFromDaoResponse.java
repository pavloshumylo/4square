package com.foursquare.validator;

import com.fasterxml.jackson.databind.JsonNode;

public class VenueValidatorFromDaoResponse {

    public static boolean isValidVenue(JsonNode venueToValidate) {
        if (venueToValidate.get("id") == null) {
            return false;
        } else if ((venueToValidate.get("name") == null) &&
                (venueToValidate.get("contact").get("phone") == null) &&
                (venueToValidate.get("location").get("address") == null)) {
            return false;
        }
        return true;
    }

    public static boolean isValidNode(JsonNode nodeToValidate) {
        return nodeToValidate != null;
    }
}
package com.foursquare.validator;

import com.fasterxml.jackson.databind.JsonNode;

public class VenueValidatorFromDaoResponse {

    public static boolean isValidVenue(JsonNode venueToValidate) {
        boolean contactNodeIsNull = true;
        if (venueToValidate.get("contact") != null && venueToValidate.get("contact").get("phone") != null) {
            contactNodeIsNull = false;
        }

        boolean locationNodeIsNull = true;
        if (venueToValidate.get("location") != null && venueToValidate.get("location").get("address") != null) {
            locationNodeIsNull = false;
        }

        if (venueToValidate.get("id") == null) {
            return false;
        } else if ((venueToValidate.get("name") == null)
                && contactNodeIsNull
                && locationNodeIsNull) {
            return false;
        }

        return true;
    }
}
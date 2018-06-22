package com.foursquare.validator;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class DaoResponseVenueValidatior {

    public static boolean isValidVenue(JsonNode venueToValidate) {
        boolean venueToValidateIsPresent = Optional.ofNullable(venueToValidate).isPresent();

        if (venueToValidateIsPresent) {
           boolean contactNodeIsNotNull = Optional.ofNullable(venueToValidate.get("contact")).map(contactNode ->
                contactNode.get("phone")).isPresent();
            boolean locationNodeIsNotNull = Optional.ofNullable(venueToValidate.get("location")).map(locationNode ->
                locationNode.get("address")).isPresent();

            return venueToValidate.get("id") != null && (venueToValidate.get("name") != null
                    || contactNodeIsNotNull || locationNodeIsNotNull);
        } else {
            return false;
        }
    }
}
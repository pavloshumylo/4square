package com.foursquare.validator;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public class VenueValidatorFromDaoResponse {

    public static boolean isValidVenue(JsonNode venueToValidate) {
        boolean venueToValidateIsPresent = Optional.ofNullable(venueToValidate).isPresent();

        if (venueToValidateIsPresent) {
            Optional<JsonNode> contactNodeOptional = Optional.ofNullable(venueToValidate.get("contact"));
            Optional<JsonNode> locationNodeOptional = Optional.ofNullable(venueToValidate.get("location"));

            boolean contactNodeIsNull = contactNodeOptional.map(contactNode -> contactNode.get("phone")).
                    map(phoneNode -> false).orElse(true);
            boolean locationNodeIsNull = locationNodeOptional.map(locationNode -> locationNode.get("address")).
                    map(addressNode -> false).orElse(true);

            if (venueToValidate.get("id") == null) {
                return false;
            } else if((venueToValidate.get("name") == null) &&  contactNodeIsNull && locationNodeIsNull) {
                return false;
            }
        }
        else return false;

        return true;
    }
}
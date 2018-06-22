package com.foursquare.dao;

import com.fasterxml.jackson.databind.JsonNode;

public interface SimilarVenuesDao {

    JsonNode getSimilarVenues(String fsId);
}
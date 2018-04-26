package com.foursquare.dao;

import com.fasterxml.jackson.databind.JsonNode;

public interface SearchDao {

     JsonNode search(String city, String query, String limit);
}

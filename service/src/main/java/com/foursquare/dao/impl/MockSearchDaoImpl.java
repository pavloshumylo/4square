package com.foursquare.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;

import java.io.IOException;
import java.io.InputStream;

public class MockSearchDaoImpl implements SearchDao {

    public String search(String city, String place, String limit) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("mockDaoResponse.json");
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return jsonNode.toString();
    }
}

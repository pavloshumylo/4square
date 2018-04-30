package com.foursquare.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;

import java.io.IOException;
import java.io.InputStream;

public class MockSearchDaoImpl implements SearchDao {

    public JsonNode search(String city, String place, String limit) {
        InputStream is = getClass().getClassLoader().getResourceAsStream("mockDaoResponse.json");

        try {
            return new ObjectMapper().readTree(is);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}

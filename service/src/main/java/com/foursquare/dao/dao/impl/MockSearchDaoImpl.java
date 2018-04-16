package com.foursquare.dao.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import java.io.IOException;
import java.io.InputStream;

public class MockSearchDaoImpl implements SearchDao {

    public String search(String city, String place) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("jsonExpectedFromDao.json");
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode.toString();
    }
}

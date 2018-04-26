package com.foursquare.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;

public class MockSearchDaoImpl implements SearchDao {

    private static final Log log = LogFactory.getLog(MockSearchDaoImpl.class);

    public String search(String city, String place, String limit) {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("mockDaoResponse.json");
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        } catch (IOException ex) {
            log.error("Exception thrown: " + ex + ", message: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
        return jsonNode.toString();
    }
}

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

    public JsonNode search(String city, String place, String limit) {
        InputStream is = getClass().getClassLoader().getResourceAsStream("mockDaoResponse.json");

        try {
            return new ObjectMapper().readTree(is);
        } catch (IOException ex) {
            log.error("Exception thrown: " + ex + ", message: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}

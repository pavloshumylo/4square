package com.foursquare.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import com.foursquare.logging.LoggingInvocation;
import com.foursquare.logging.LoggingLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public class MockSearchDaoImpl implements SearchDao {

    private static final Logger LOG = LoggerFactory.getLogger(MockSearchDaoImpl.class);

    @LoggingInvocation(logLevel = LoggingLevel.INFO)
    public JsonNode search(String city, String place, String limit) {
        InputStream is = getClass().getClassLoader().getResourceAsStream("mockDaoResponse.json");

        try {
            return new ObjectMapper().readTree(is);
        } catch (IOException ex) {
            LOG.error("Exception thrown: " + ex + ", message: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}

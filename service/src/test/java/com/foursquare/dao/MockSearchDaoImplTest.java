package com.foursquare.dao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.dao.impl.MockSearchDaoImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class MockSearchDaoImplTest {

    private SearchDao searchDao;
    private String jsonExpected;

    @Before
    public void init() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("expectedMockDaoResponse.json");
        JsonNode jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        jsonExpected = jsonNode.get("testJsons").get(0).toString();
        searchDao = new MockSearchDaoImpl();
    }

    @Test
    public void testSearch_ShouldReturnJson() {
        String jsonReturned = searchDao.search("testCity", "testPlace", "testLimit");
        assertEquals(jsonExpected, jsonReturned);
    }
}

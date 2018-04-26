package com.foursquare.dao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.impl.MockSearchDaoImpl;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class MockSearchDaoImplTest {

    @Test
    public void testSearch_ShouldReturnJson() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/mock_search_dao_response.json");
        JsonNode jsonNodeExpected = new ObjectMapper().readTree(is);

        JsonNode jsonNodeReturned = new MockSearchDaoImpl().search("testCity", "testPlace", "testLimit");
        assertEquals(jsonNodeExpected, jsonNodeReturned);
    }
}

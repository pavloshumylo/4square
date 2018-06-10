package com.foursquare.dao;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.impl.MockSearchDaoImpl;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class MockSearchDaoImplTest {

    private ClassLoader classLoader;
    private InputStream is;
    private JsonNode jsonNodeExpected, jsonNodeReturned;

    @Before
    public void init() {
        classLoader = getClass().getClassLoader();
    }

    @Test
    public void testSearchFirst_ShouldReturnJson() throws IOException {
        is = classLoader.getResourceAsStream("testData/mock_search_dao_response.json");
        jsonNodeExpected = new ObjectMapper().readTree(is);

        jsonNodeReturned = new MockSearchDaoImpl().search("testCity", "testPlace", "testLimit");
        assertEquals(jsonNodeExpected, jsonNodeReturned);
    }

    @Test
    public void testSearchSecond_ShouldReturnJson() throws IOException {
        is = classLoader.getResourceAsStream("testData/mock_dao_response_venue_by_fs_id.json");
        jsonNodeExpected = new ObjectMapper().readTree(is);

        jsonNodeReturned = new MockSearchDaoImpl().search("4bec2c3062c0c92865ffe2d4");
        assertEquals(jsonNodeExpected, jsonNodeReturned);
    }
}

package com.foursquare.dao;
import static org.junit.Assert.*;

import com.foursquare.dao.dao.impl.MockSearchDaoImpl;
import org.junit.Before;
import org.junit.Test;

public class MockSearchDaoImplTest {

    private SearchDao searchDao;
    private String jsonExpected;

    @Before
    public void init() {
        jsonExpected = "{\n" +
                "    \"meta\": {\n" +
                "        \"code\": 200,\n" +
                "        \"requestId\": \"5ac5eb54351e3d4df8cebb05\"\n" +
                "    },\n" +
                "    \"response\": {\n" +
                "        \"venues\": [\n" +
                "            {\n" +
                "                \"id\": \"535a021d498ed71c77ed20e7\",\n" +
                "                \"name\": \"Нова пошта (відділення №14)\",\n" +
                "                \"contact\": {\n" +
                "                    \"phone\": \"+380322901911\",\n" +
                "                    \"formattedPhone\": \"+380 322 901 911\",\n" +
                "                    \"twitter\": \"_novaposhta_\"\n" +
                "                },\n" +
                "                \"location\": {\n" +
                "                    \"address\": \"вул. Словацького, 5\",\n" +
                "                    \"crossStreet\": \"вул. Петра Дорошенка\",\n" +
                "                    \"lat\": 49.83832099953952,\n" +
                "                    \"lng\": 24.023289687789234\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "                \"id\": \"Test 2 id\",\n" +
                "                \"name\": \"Test 2 name\",\n" +
                "                \"contact\": {\n" +
                "                    \"phone\": \"Test 2 number\",\n" +
                "                    \"formattedPhone\": \"+380 322 901 911\",\n" +
                "                    \"twitter\": \"_novaposhta_\"\n" +
                "                },\n" +
                "                \"location\": {\n" +
                "                    \"address\": \"Test 2 address\",\n" +
                "                    \"crossStreet\": \"вул. Петра Дорошенка\",\n" +
                "                    \"lat\": 49.83832099953952,\n" +
                "                    \"lng\": 24.023289687789234\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "                \"id\": \"Test 3 id\",\n" +
                "                \"name\": \"Test 3 name\",\n" +
                "                \"contact\": {\n" +
                "                    \"phone\": \"Test 3 number\",\n" +
                "                    \"formattedPhone\": \"+380 322 901 911\",\n" +
                "                    \"twitter\": \"_novaposhta_\"\n" +
                "                },\n" +
                "                \"location\": {\n" +
                "                    \"address\": \"Test 3 address\",\n" +
                "                    \"crossStreet\": \"вул. Петра Дорошенка\",\n" +
                "                    \"lat\": 49.83832099953952,\n" +
                "                    \"lng\": 24.023289687789234\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t},\n" +
                "\t\t\t{\n" +
                "                \"id\": \"Test 4 id\",\n" +
                "                \"name\": \"Test 4 name\",\n" +
                "                \"contact\": {\n" +
                "                    \"phone\": \"Test 4 number\",\n" +
                "                    \"formattedPhone\": \"+380 322 901 911\",\n" +
                "                    \"twitter\": \"_novaposhta_\"\n" +
                "                },\n" +
                "                \"location\": {\n" +
                "                    \"address\": \"Test 4 address\",\n" +
                "                    \"crossStreet\": \"вул. Петра Дорошенка\",\n" +
                "                    \"lat\": 49.83832099953952,\n" +
                "                    \"lng\": 24.023289687789234\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t\t\t\t]\n" +
                "\t}\n" +
                "}";
        searchDao = new MockSearchDaoImpl();
    }

    @Test
    public void testSearch_ShouldReturnJson() {
        String jsonReturned = searchDao.search("testCity", "testPlace");
        assertEquals(jsonExpected, jsonReturned);
    }
}

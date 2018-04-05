package com.dao;

import org.springframework.stereotype.Repository;

@Repository
public class SearchDao {

    public String search(String city, String place) {
        String json = "{\n" +
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
                "\t\t\t}\n" +
                "\t\t\t\t\t]\n" +
                "\t}\n" +
                "}";

        return json;
    }
}

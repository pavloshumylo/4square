package com.foursquare.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import com.foursquare.config.FourSquareProperties;
import com.foursquare.logging.LoggingInvocation;
import com.foursquare.logging.LoggingLevels;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

public class SearchDaoImpl implements SearchDao {

    private static final Log log = LogFactory.getLog(SearchDaoImpl.class);

    @Autowired
    private FourSquareProperties fourSquareProperties;

    @Autowired
    private RestTemplate restTemplate;

    private static final String url = "{fourSquareApiHost}v2/venues/search?near={city}&query={query}&limit={limit}&client_id={client_id}&client_secret={client_secret}&v={version}";

    @LoggingInvocation(logLevel = LoggingLevels.INFO)
    public JsonNode search(String city, String query, String limit) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity response = restTemplate.exchange(url,
                    HttpMethod.GET,
                    httpEntity,
                    String.class,
                    fourSquareProperties.getApiHost(),
                    city,
                    query,
                    limit,
                    fourSquareProperties.getApiClientId(),
                    fourSquareProperties.getApiClientSecret(),
                    fourSquareProperties.getApiVersion());

        try {
            return new ObjectMapper().readTree(response.getBody().toString());
        } catch (IOException ex) {
            log.error("Exception thrown: " + ex + ", message: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}

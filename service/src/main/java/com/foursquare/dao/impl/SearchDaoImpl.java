package com.foursquare.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.config.FourSquareProperties;
import com.foursquare.dao.SearchDao;
import com.foursquare.logging.LoggingInvocation;
import com.foursquare.logging.LoggingLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class SearchDaoImpl implements SearchDao {

    private static final Logger LOG = LoggerFactory.getLogger(SearchDaoImpl.class);

    @Autowired
    private FourSquareProperties fourSquareProperties;

    @Autowired
    private RestTemplate restTemplate;

    private static final String urlSearchByParams = "{fourSquareApiHost}v2/venues/search?near={city}&query={query}&limit={limit}&client_id={client_id}&client_secret={client_secret}&v={version}";
    private static final String urlSearchByFsId = "{fourSquareApiHost}v2/venues/{foursquare_id}?client_id={client_id}&client_secret={client_secret}&v={version}";

    @LoggingInvocation(logLevel = LoggingLevel.INFO)
    public JsonNode search(String city, String query, String limit) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity response = restTemplate.exchange(urlSearchByParams,
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
        } catch (Exception ex) {
            LOG.error("Exception thrown: " + ex + ", message: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }

    @LoggingInvocation(logLevel = LoggingLevel.INFO)
    public JsonNode search(String fsId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity response = restTemplate.exchange(urlSearchByFsId,
                HttpMethod.GET,
                httpEntity,
                String.class,
                fourSquareProperties.getApiHost(),
                fsId,
                fourSquareProperties.getApiClientId(),
                fourSquareProperties.getApiClientSecret(),
                fourSquareProperties.getApiVersion());

        try {
            return new ObjectMapper().readTree(response.getBody().toString());
        } catch (Exception ex) {
            LOG.error("Exception thrown: " + ex + ", message: " + ex.getMessage());
            throw new RuntimeException(ex);
        }
    }
}

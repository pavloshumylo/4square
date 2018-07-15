package com.foursquare.dao.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.config.FourSquareProperties;
import com.foursquare.dao.SimilarVenuesDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class SimilarVenuesDaoImpl implements SimilarVenuesDao {

    @Autowired
    private FourSquareProperties fourSquareProperties;

    private static final String urlSimilarVenues = "{fourSquareApiHost}v2/venues/{venue_id}/similar?client_id={client_id}&client_secret={client_secret}&v={version}";

    public JsonNode getSimilarVenues(String fsId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ResponseEntity response = new RestTemplate().exchange(urlSimilarVenues,
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
            throw new RuntimeException(ex);
        }
    }
}
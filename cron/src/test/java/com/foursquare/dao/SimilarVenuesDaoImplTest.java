package com.foursquare.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.config.FourSquareProperties;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimilarVenuesDaoImplTest {

    private static final String APPLICATION_JSON_VALUE ="application/json;charset=UTF-8";

    @Autowired
    private SimilarVenuesDao similarVenuesDao;
    @Autowired
    private FourSquareProperties fourSquareProperties;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Before
    public void init() {
        fourSquareProperties.setApiHost("http://localhost:"+wireMockRule.port()+"/");
    }

    @Test
    public void testGetSimilarVenues_ShouldReturnJsonNode() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("testData/similar_dao_response_for_wire_mock_body_response.json");
        String responseExpected = new ObjectMapper().readTree(is).toString();

        stubFor(get(urlMatching("/v2/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(responseExpected)));

        JsonNode responseActual = similarVenuesDao.getSimilarVenues("fsId");
        assertEquals(new ObjectMapper().readTree(responseExpected), responseActual);
    }

    @Test(expected = RuntimeException.class)
    public void testGetSimilarVenues_ShouldThrowRuntimeException() {
        stubFor(get(urlMatching("/v2/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody("")));
        similarVenuesDao.getSimilarVenues("fsId");
    }
}

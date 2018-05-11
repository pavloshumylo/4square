package com.foursquare.dao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.config.FourSquareProperties;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("db")
public class SearchDaoImplTest {

    private static final String APPLICATION_JSON_VALUE ="application/json;charset=UTF-8";

    @Autowired
    private SearchDao searchDao;
    @Autowired
    private FourSquareProperties fourSquareProperties;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Before
    public void init() {
        fourSquareProperties.setApiHost("http://localhost:"+wireMockRule.port()+"/");
    }

    @Test
    public void testSearch_ShouldReturnJson() throws IOException {
        InputStream is = getClass().getClassLoader().getResourceAsStream("testData/search_dao_impl_wire_mock_body_response.json");
        String responseExpected = IOUtils.toString(is, StandardCharsets.UTF_8.name());

        stubFor(get(urlMatching("/v2/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(responseExpected)));

        JsonNode responseActual = searchDao.search("queryFirst", "querySecond", "queryThird");
        assertEquals(new ObjectMapper().readTree(responseExpected), responseActual);
    }

    @Test(expected = NullPointerException.class)
    public void testSearch_ShouldThrowNullPointerException() {
            stubFor(get(urlMatching("/v2/venues/.*"))
                    .willReturn(aResponse()
                            .withStatus(200)
                            .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                            .withBody("")));
            searchDao.search("queryFirst", "querySecond", "queryThird");
    }
}
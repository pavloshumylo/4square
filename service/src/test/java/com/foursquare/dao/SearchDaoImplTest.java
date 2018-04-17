package com.foursquare.dao;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchDaoImplTest {

    @Autowired
    private SearchDao searchDao;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(8090);

    @Test
    public void testSearch_ShouldReturnJson() throws IOException {
        String responseExpected = "\"name\": \"Malevich\"";
        stubFor(get(urlMatching("/v2/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                        .withBody(responseExpected)));
        String responseActual = searchDao.search("queryFirst", "querySecond", "queryThird");
        assertEquals(responseExpected, responseActual);
    }
}
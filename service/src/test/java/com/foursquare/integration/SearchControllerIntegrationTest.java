package com.foursquare.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.config.FourSquareProperties;
import com.foursquare.controller.SearchController;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchControllerIntegrationTest {

    private static final String APPLICATION_JSON_VALUE ="application/json;charset=UTF-8";

    @Autowired
    private SearchController searchController;
    @Autowired
    private FourSquareProperties fourSquareProperties;

    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Before
    public void init() throws IOException {
        fourSquareProperties.setApiHost("http://localhost:"+wireMockRule.port()+"/");
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
    }

    @Test
    public void testController_ShouldReturnSearchResponseDto() throws Exception {
        SearchResponseDto searchResponseDtoExpected = new SearchResponseDto();
        VenueDto venueDtoExpected = new VenueDto();
        venueDtoExpected.setId("535a021d498ed71c77ed20e7");
        venueDtoExpected.setName("Нова пошта (відділення №14)");
        venueDtoExpected.setPhone("+380322901911");
        venueDtoExpected.setAddress("вул. Словацького, 5");
        searchResponseDtoExpected.getVenues().add(venueDtoExpected);

        venueDtoExpected = new VenueDto();
        venueDtoExpected.setId("Test 2 id");
        venueDtoExpected.setName("Test 2 name");
        venueDtoExpected.setPhone("Test 2 number");
        venueDtoExpected.setAddress("Test 2 address");
        searchResponseDtoExpected.getVenues().add(venueDtoExpected);

        venueDtoExpected = new VenueDto();
        venueDtoExpected.setId("Test 3 id");
        venueDtoExpected.setName("Test 3 name");
        venueDtoExpected.setPhone("Test 3 number");
        venueDtoExpected.setAddress("Test 3 address");
        searchResponseDtoExpected.getVenues().add(venueDtoExpected);

        venueDtoExpected = new VenueDto();
        venueDtoExpected.setId("Test 4 id");
        venueDtoExpected.setName("Test 4 name");
        venueDtoExpected.setPhone("Test 4 number");
        venueDtoExpected.setAddress("Test 4 address");
        searchResponseDtoExpected.getVenues().add(venueDtoExpected);

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/search_controller_integration_response.json");
        String jsonExpected = IOUtils.toString(is, StandardCharsets.UTF_8.name());

        stubFor(WireMock.get(urlMatching("/v2/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(jsonExpected)));

        MvcResult mvcResult = mockMvc.perform(get("/search")
                .param("near", "testCity")
                .param("query", "testPlace")
                .param("limit", "testLimit"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        SearchResponseDto searchResponseDtoActual = new ObjectMapper().readValue(responseString, SearchResponseDto.class);

        assertEquals(searchResponseDtoExpected, searchResponseDtoActual);

        assertEquals(searchResponseDtoExpected.getVenues().size(),
                searchResponseDtoActual.getVenues().size());

        assertEquals(searchResponseDtoExpected.getVenues().get(0).getId(),
                searchResponseDtoActual.getVenues().get(0).getId());

        assertEquals(searchResponseDtoExpected.getVenues().get(0).getName(),
                searchResponseDtoActual.getVenues().get(0).getName());

        assertEquals(searchResponseDtoExpected.getVenues().get(0).getPhone(),
                searchResponseDtoActual.getVenues().get(0).getPhone());

        assertEquals(searchResponseDtoExpected.getVenues().get(0).getAddress(),
                searchResponseDtoActual.getVenues().get(0).getAddress());
    }
}

package com.foursquare.endpoints;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.endpoints.impl.FoursquareClientEndpointsImpl;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FoursquareClientEndpointsImplTest {

    @Autowired
    private FoursquareClientEndpointsImpl foursquareClientEndpoints;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Before
    public void init() {
        foursquareClientEndpoints.setHost("http://localhost:"+wireMockRule.port()+"/");
    }

    @Test
    public void testInvokeSearchEndPoint_ShouldReturnCompletableFutureWithSearchResponseDto() throws IOException, ExecutionException, InterruptedException {
        InputStream inputStreamWithSearchResponseDtoFirst = getClass().getClassLoader()
                .getResourceAsStream("testData/search_response_dto.json");

        InputStream inputStreamWithSearchResponseDtoSecond = getClass().getClassLoader()
                .getResourceAsStream("testData/search_response_dto.json");

        SearchResponseDto searchResponseDtoExpected = new ObjectMapper().readValue(inputStreamWithSearchResponseDtoFirst, SearchResponseDto.class);

        stubFor(WireMock.get(urlMatching("/search?.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().readTree(inputStreamWithSearchResponseDtoSecond).toString())));

        CompletableFuture<ResponseEntity<SearchResponseDto>> completableFutureActual = foursquareClientEndpoints
                .invokeSearchEndPoint("city", "query",  "limit", "accessToken");

        assertEquals(searchResponseDtoExpected, completableFutureActual.get().getBody());
    }

    @Test
    public void testInvokeRegistrationEndPoint_ShouldReturnCompletableFutureWithOkResponseEntity() throws ExecutionException, InterruptedException {
        stubFor(WireMock.post(urlMatching("/registration"))
                        .willReturn(aResponse()
                        .withStatus(200)));

        CompletableFuture<ResponseEntity<ResponseEntity>> completableFutureActual = foursquareClientEndpoints.invokeRegistrationEndPoint(new User());

        assertEquals(HttpStatus.OK, completableFutureActual.get().getStatusCode());
    }

    @Test
    public void testInvokeSaveEndpoint_ShouldReturnCompletableFutureWithOkResponseEntity() throws ExecutionException, InterruptedException {
        stubFor(WireMock.post(urlMatching("/venues/.*"))
                        .willReturn(aResponse()
                        .withStatus(200)));

        CompletableFuture<ResponseEntity<ResponseEntity>> completableFutureActual = foursquareClientEndpoints
                .invokeSaveEndpoint("fsId", "accessToken");

        assertEquals(HttpStatus.OK, completableFutureActual.get().getStatusCode());
    }

    @Test
    public void testInvokeDeleteEndpoint_ShouldReturnCompletableFutureWithOkResponseEntity() throws ExecutionException, InterruptedException {
        stubFor(WireMock.delete(urlMatching("/venues/.*"))
                        .willReturn(aResponse()
                        .withStatus(200)));

        CompletableFuture<ResponseEntity<ResponseEntity>> completableFutureActual = foursquareClientEndpoints
                .invokeDeleteEndpoint("fsId", "accessToken");

        assertEquals(HttpStatus.OK, completableFutureActual.get().getStatusCode());
    }

    @Test
    public void testInvokeGetEndpoint_ShouldReturnCompletableFutureWithListOfVenues() throws IOException, ExecutionException, InterruptedException {
        InputStream inputStreamWithListOfVenuesFirst = getClass().getClassLoader()
                .getResourceAsStream("testData/list_of_venues.json");

        InputStream inputStreamWithListOfVenuesSecond = getClass().getClassLoader()
                .getResourceAsStream("testData/list_of_venues.json");

        List<Venue> venuesExpected = new ObjectMapper().readValue(inputStreamWithListOfVenuesFirst, new TypeReference<List<Venue>>() {});

        stubFor(WireMock.get(urlMatching("/venues?.*"))
                        .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().readTree(inputStreamWithListOfVenuesSecond).toString())));

        CompletableFuture<ResponseEntity<List<Venue>>> completableFutureActual = foursquareClientEndpoints.invokeGetEndpoint(null);

        assertEquals(venuesExpected, completableFutureActual.get().getBody());
    }
}
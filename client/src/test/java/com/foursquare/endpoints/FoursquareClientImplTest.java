package com.foursquare.endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.foursquare.endpoints.impl.FoursquareClientImpl;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoursquareClientImpl.class)
public class FoursquareClientImplTest {

    @Autowired
    private FoursquareClientImpl foursquareClientEndpoints;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Before
    public void init() {
        Field field = ReflectionUtils.findField(FoursquareClientImpl.class, "baseEndPointsUrl");
        field.setAccessible(true);
        ReflectionUtils.setField(field, foursquareClientEndpoints, "http://localhost:"+wireMockRule.port());
    }

    @Test
    public void testInvokeSearchEndPoint_ShouldReturnCompletableFutureWithSearchResponseDto() throws IOException, ExecutionException, InterruptedException {
        VenueDto firstVenueDto = new VenueDto();
        VenueDto secondVenueDto = new VenueDto();

        firstVenueDto.setId("first");
        firstVenueDto.setName("nameFirst");
        firstVenueDto.setPhone("phoneFirst");
        firstVenueDto.setAddress("addressFirst");

        secondVenueDto.setId("second");
        secondVenueDto.setName("nameSecond");
        secondVenueDto.setPhone("phoneSecond");
        secondVenueDto.setAddress("addressSecond");

        SearchResponseDto searchResponseDtoExpected = new SearchResponseDto();
        searchResponseDtoExpected.getVenues().addAll(Arrays.asList(firstVenueDto, secondVenueDto));

        InputStream inputStreamWithSearchResponseDto = getClass().getClassLoader()
                .getResourceAsStream("testData/search_response_dto.json");

        stubFor(WireMock.get(urlMatching("/search?.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().readTree(inputStreamWithSearchResponseDto).toString())));

        CompletableFuture<SearchResponseDto> completableFutureActual = foursquareClientEndpoints
                .invokeSearchEndPoint("city", "query",  "limit", "accessToken");

        assertEquals(searchResponseDtoExpected, completableFutureActual.get());
    }

    @Test
    public void testInvokeRegistrationEndPoint_ShouldReturnCompletableFutureWithOkResponseEntity() throws ExecutionException, InterruptedException {
        stubFor(WireMock.post(urlMatching("/registration"))
                        .willReturn(aResponse()
                        .withStatus(200)));

        CompletableFuture<Void> completableFutureActual = foursquareClientEndpoints.invokeRegistrationEndPoint(new User());

        assertNull(completableFutureActual.get());
    }

    @Test
    public void testInvokeSaveEndpoint_ShouldReturnCompletableFutureWithOkResponseEntity() throws ExecutionException, InterruptedException {
        stubFor(WireMock.post(urlMatching("/venues/.*"))
                        .willReturn(aResponse()
                        .withStatus(200)));

        CompletableFuture<Void> completableFutureActual = foursquareClientEndpoints
                .invokeSaveEndpoint("fsId", "accessToken");

        assertNull(completableFutureActual.get());
    }

    @Test
    public void testInvokeDeleteEndpoint_ShouldReturnCompletableFutureWithOkResponseEntity() throws ExecutionException, InterruptedException {
        stubFor(WireMock.delete(urlMatching("/venues/.*"))
                        .willReturn(aResponse()
                        .withStatus(200)));

        CompletableFuture<Void> completableFutureActual = foursquareClientEndpoints
                .invokeDeleteEndpoint("fsId", "accessToken");

        assertNull(completableFutureActual.get());
    }

    @Test
    public void testInvokeGetEndpoint_ShouldReturnCompletableFutureWithListOfVenues() throws IOException, ExecutionException, InterruptedException {
        Venue venueFirst = new Venue();
        Venue venueSecond = new Venue();
        Venue venueThird = new Venue();

        venueFirst.setId(1);
        venueFirst.setName("firstName");
        venueFirst.setFsId("firstFsId");

        venueSecond.setId(2);
        venueSecond.setName("secondName");
        venueSecond.setFsId("secondFsId");

        venueThird.setId(3);
        venueThird.setName("thirdName");
        venueThird.setFsId("thirdFsId");

        List<Venue> venuesExpected = Arrays.asList(venueFirst, venueSecond, venueThird);

        InputStream inputStreamWithListOfVenues = getClass().getClassLoader()
                .getResourceAsStream("testData/list_of_venues.json");

        stubFor(WireMock.get(urlMatching("/venues?.*"))
                        .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().readTree(inputStreamWithListOfVenues).toString())));

        CompletableFuture<List<Venue>> completableFutureActual = foursquareClientEndpoints.invokeGetEndpoint("accessToken");

        assertEquals(venuesExpected, completableFutureActual.get());
    }

    @Test
    public void testInvokeSearchEndPoint_ShouldThrowHttpClientErrorException() throws InterruptedException {
        stubFor(WireMock.get(urlMatching("/search?.*"))
                .willReturn(aResponse()
                        .withStatus(400)));

        try {
            foursquareClientEndpoints
                    .invokeSearchEndPoint("city", "query", "limit", "accessToken").get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpClientErrorException);
        }
    }

    @Test
    public void testInvokeRegistrationEndPoint_ShouldThrowHttpServerErrorException() throws InterruptedException {
        stubFor(WireMock.post(urlMatching("/registration"))
                .willReturn(aResponse()
                        .withStatus(500)));

        try {
            foursquareClientEndpoints.invokeRegistrationEndPoint(new User()).get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpServerErrorException);
        }
    }

    @Test
    public void testInvokeSaveEndpoint_ShouldThrowHttpClientErrorException() throws InterruptedException {
        stubFor(WireMock.post(urlMatching("/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(409)));

        try {
            foursquareClientEndpoints
                    .invokeSaveEndpoint("fsId", "accessToken").get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpClientErrorException);
        }
    }

    @Test
    public void testInvokeDeleteEndpoint_ShouldThrowHttpServerErrorException() throws InterruptedException {
        stubFor(WireMock.delete(urlMatching("/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(503)));

        try {
            foursquareClientEndpoints
                    .invokeDeleteEndpoint("fsId", "accessToken").get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpServerErrorException);
        }
    }

    @Test
    public void testInvokeGetEndpoint_ShouldThrowHttpClientErrorException() throws InterruptedException {
        stubFor(WireMock.get(urlMatching("/venues?.*"))
                .willReturn(aResponse()
                        .withStatus(400)));

        try {
            foursquareClientEndpoints.invokeGetEndpoint("accessToken").get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpClientErrorException);
        }
    }

    @Test(expected = CancellationException.class)
    public void testbuildCompletableFuture_Cancel_ShouldThrowCancellationException() throws InterruptedException, ExecutionException {
            CompletableFuture completableFutureActual = foursquareClientEndpoints.invokeGetEndpoint("accessToken");
            completableFutureActual.cancel(true);
            completableFutureActual.get();
    }
}
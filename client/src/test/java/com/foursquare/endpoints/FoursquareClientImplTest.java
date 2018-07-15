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
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FoursquareClientImpl.class)
public class FoursquareClientImplTest {

    @Autowired
    private FoursquareClientImpl foursquareClientEndpoints;

    @SpyBean
    private AsyncRestTemplate asyncRestTemplate;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Before
    public void init() {
        Field field = ReflectionUtils.findField(FoursquareClientImpl.class, "baseEndPointsUrl");
        field.setAccessible(true);
        ReflectionUtils.setField(field, foursquareClientEndpoints, "http://localhost:"+wireMockRule.port());
    }

    @Test
    public void testSearchVenuesByParams_ShouldReturnCompletableFutureWithSearchResponseDto() throws IOException, ExecutionException, InterruptedException {
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
                .searchVenuesByParams("city", "query",  "limit", "accessToken");

        assertEquals(searchResponseDtoExpected, completableFutureActual.get());
    }

    @Test
    public void testRegisterNewUser_ShouldReturnCompletableFutureWithOkResponseEntity() throws ExecutionException, InterruptedException {
        stubFor(WireMock.post(urlMatching("/registration"))
                        .willReturn(aResponse()
                        .withStatus(200)));

        CompletableFuture<Void> completableFutureActual = foursquareClientEndpoints.registerNewUser(new User());

        assertNull(completableFutureActual.get());
    }

    @Test
    public void testSaveVenueForUser_ShouldReturnCompletableFutureWithOkResponseEntity() throws ExecutionException, InterruptedException {
        stubFor(WireMock.post(urlMatching("/venues/.*"))
                        .willReturn(aResponse()
                        .withStatus(200)));

        CompletableFuture<Void> completableFutureActual = foursquareClientEndpoints
                .saveVenueForUser("fsId", "accessToken");

        assertNull(completableFutureActual.get());
    }

    @Test
    public void testRemoveVenueForUser_ShouldReturnCompletableFutureWithOkResponseEntity() throws ExecutionException, InterruptedException {
        stubFor(WireMock.delete(urlMatching("/venues/.*"))
                        .willReturn(aResponse()
                        .withStatus(200)));

        CompletableFuture<Void> completableFutureActual = foursquareClientEndpoints
                .removeVenueForUser("fsId", "accessToken");

        assertNull(completableFutureActual.get());
    }

    @Test
    public void testGetAllVenuesForUser_ShouldReturnCompletableFutureWithListOfVenues() throws IOException, ExecutionException, InterruptedException {
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

        CompletableFuture<List<Venue>> completableFutureActual = foursquareClientEndpoints.getAllVenuesForUser("accessToken");

        assertEquals(venuesExpected, completableFutureActual.get());
    }

    @Test
    public void testSearchVenuesByParams_ShouldThrowHttpClientErrorException() throws InterruptedException {
        stubFor(WireMock.get(urlMatching("/search?.*"))
                .willReturn(aResponse()
                        .withStatus(400)));

        try {
            foursquareClientEndpoints
                    .searchVenuesByParams("city", "query", "limit", "accessToken").get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpClientErrorException);
        }
    }

    @Test
    public void testRegisterNewUser_ShouldThrowHttpServerErrorException() throws InterruptedException {
        stubFor(WireMock.post(urlMatching("/registration"))
                .willReturn(aResponse()
                        .withStatus(500)));

        try {
            foursquareClientEndpoints.registerNewUser(new User()).get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpServerErrorException);
        }
    }

    @Test
    public void testSaveVenueForUser_ShouldThrowHttpClientErrorException() throws InterruptedException {
        stubFor(WireMock.post(urlMatching("/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(409)));

        try {
            foursquareClientEndpoints
                    .saveVenueForUser("fsId", "accessToken").get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpClientErrorException);
        }
    }

    @Test
    public void testRemoveVenueForUser_ShouldThrowHttpServerErrorException() throws InterruptedException {
        stubFor(WireMock.delete(urlMatching("/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(503)));

        try {
            foursquareClientEndpoints
                    .removeVenueForUser("fsId", "accessToken").get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpServerErrorException);
        }
    }

    @Test
    public void testGetAllVenuesForUser_ShouldThrowHttpClientErrorException() throws InterruptedException {
        stubFor(WireMock.get(urlMatching("/venues?.*"))
                .willReturn(aResponse()
                        .withStatus(400)));

        try {
            foursquareClientEndpoints.getAllVenuesForUser("accessToken").get();

            Assert.fail();
        }
        catch (ExecutionException ex) {
            assertTrue(ex.getCause() instanceof HttpClientErrorException);
        }
    }

    @Test
    public void testListenableFutureCancelInvocation_ShouldInvokeListenableCancelOnce() {
        ListenableFuture listenableFuture = mock(ListenableFuture.class);
        doReturn(listenableFuture).when(asyncRestTemplate).exchange(anyString(), any(HttpMethod.class), any(HttpEntity.class), any(ParameterizedTypeReference.class), anyString(), anyString());
        CompletableFuture completableFutureActual = foursquareClientEndpoints.getAllVenuesForUser("accessToken");
        completableFutureActual.cancel(true);

        verify(listenableFuture).cancel(true);
    }
}
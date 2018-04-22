package com.foursquare.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.foursquare.service.impl.SearchServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class SearchServiceImplTest {

    @InjectMocks
    private SearchServiceImpl searchService;

    @Mock
    private SearchDao searchDao;

    private SearchResponseDto searchResponseDtoExpected;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        searchResponseDtoExpected = new SearchResponseDto();
    }

    @Test
    public void testSearchService_ShouldReturnSearchResponseDto() throws IOException {
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
        InputStream is = classLoader.getResourceAsStream("search_service_response_normal.json");
        JsonNode jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        String jsonFromDao = jsonNode.toString();

        when(searchDao.search(any(String.class), any(String.class), any(String.class))).thenReturn(jsonFromDao);
        SearchResponseDto searchResponseDtoActual = searchService.search("testCity", "testPlace", "testLimit");

        assertEquals(searchResponseDtoExpected, searchResponseDtoActual);

        assertEquals(searchResponseDtoExpected.getVenues().size(),
                searchResponseDtoActual.getVenues().size());

        assertEquals(searchResponseDtoExpected.getVenues().get(0).getId(),
                searchResponseDtoActual.getVenues().get(0).getId());

        assertEquals(searchResponseDtoExpected.getVenues().get(0).getAddress(),
                searchResponseDtoActual.getVenues().get(0).getAddress());

        assertEquals(searchResponseDtoExpected.getVenues().get(0).getName(),
                searchResponseDtoActual.getVenues().get(0).getName());

        assertEquals(searchResponseDtoExpected.getVenues().get(0).getPhone(),
                searchResponseDtoActual.getVenues().get(0).getPhone());
    }

    @Test
    public void testSearchService_ShouldReturnEmptySearchResponseDto() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("search_service_response_with_empty_venues.json");
        JsonNode jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        String jsonFromDao = jsonNode.toString();

        when(searchDao.search(any(String.class), any(String.class), any(String.class))).thenReturn(jsonFromDao);
        SearchResponseDto searchResponseDtoActual = searchService.search("testCity", "testPlace", "testLimit");

        assertEquals(searchResponseDtoExpected, searchResponseDtoActual);

        assertEquals(searchResponseDtoExpected.getVenues().size(),
                searchResponseDtoActual.getVenues().size());
    }

    @Test
    public void testSearchService_ShouldReturnDtosWhereIdPresent() throws IOException {
        VenueDto venueDtoExpected = new VenueDto();
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
        InputStream is = classLoader.getResourceAsStream("search_service_response_first_venue_without_id.json");
        JsonNode jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        String jsonFromDao = jsonNode.toString();

        when(searchDao.search(any(String.class), any(String.class), any(String.class))).thenReturn(jsonFromDao);
        SearchResponseDto searchResponseDtoActual = searchService.search("testCity", "testPlace", "testLimit");

        assertEquals(searchResponseDtoExpected, searchResponseDtoActual);
    }

    @Test
    public void testSearchService_ShouldReturnDtoWithIdNamePhoneAddress() throws IOException {
        VenueDto venueDtoExpected = new VenueDto();
        venueDtoExpected.setId("Test 2 id");
        venueDtoExpected.setName("Test 2 name");
        venueDtoExpected.setPhone("Test 2 number");
        venueDtoExpected.setAddress("Test 2 address");
        searchResponseDtoExpected.getVenues().add(venueDtoExpected);

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.
                getResourceAsStream("search_service_response_first_venue_without_name_phone_address.json");
        JsonNode jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        String jsonFromDao = jsonNode.toString();

        when(searchDao.search(any(String.class), any(String.class), any(String.class))).thenReturn(jsonFromDao);
        SearchResponseDto searchResponseDtoActual = searchService.search("testCity", "testPlace", "testLimit");

        assertEquals(searchResponseDtoExpected, searchResponseDtoActual);
    }

    @Test
    public void testSearchService_ShouldReturnDtoWithNullAtNameAddressFields() throws IOException {
        VenueDto venueDtoExpected = new VenueDto();
        venueDtoExpected.setId("535a021d498ed71c77ed20e7");
        venueDtoExpected.setName(null);
        venueDtoExpected.setPhone("+380322901911");
        venueDtoExpected.setAddress(null);
        searchResponseDtoExpected.getVenues().add(venueDtoExpected);

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("search_service_response_missing_name_address.json");
        JsonNode jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        String jsonFromDao = jsonNode.toString();

        when(searchDao.search(any(String.class), any(String.class), any(String.class))).thenReturn(jsonFromDao);
        SearchResponseDto searchResponseDtoActual = searchService.search("testCity", "testPlace", "testLimit");

        assertEquals(searchResponseDtoExpected, searchResponseDtoActual);
    }

    @Test
    public void testSearchService_ShouldThrowNullPointerException() {
        when(searchDao.search(any(String.class), any(String.class), any(String.class))).thenReturn(null);
        try {
            searchService.search("testCity", "testPlace", "testLimit");
            Assert.fail();
        } catch (Exception ex) {
            assertTrue(ex.getCause() instanceof NullPointerException);
        }
    }

    @Test
    public void testSearchService_ShouldThrowJsonParseException() {
        when(searchDao.search(any(String.class), any(String.class), any(String.class))).thenReturn("badjson");
        try {
            searchService.search("testCity", "testPlace", "testLimit");
            Assert.fail();
        } catch (Exception ex) {
            assertTrue(ex.getCause() instanceof JsonParseException);
        }
    }
}

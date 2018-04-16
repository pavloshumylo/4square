package com.foursquare.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.foursquare.service.service.impl.SearchServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class SearchServiceImplTest {

    @InjectMocks
    private SearchServiceImpl searchService;

    @Mock
    private SearchDao searchDao;

    private String jsonFromDao;
    private SearchResponseDto searchResponseDtoExpected;

    @Before
    public void init() throws IOException {
        MockitoAnnotations.initMocks(this);
        searchResponseDtoExpected = new SearchResponseDto();

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
        InputStream is = classLoader.getResourceAsStream("expectedMockDaoResponse.json");
        JsonNode jsonNode = new ObjectMapper().readValue(is, JsonNode.class);
        jsonFromDao = jsonNode.toString();
    }

    @Test
    public void testSearchService_ShouldReturnSearchResponseDto() {
        when(searchDao.search(any(String.class), any(String.class))).thenReturn(jsonFromDao);
        SearchResponseDto searchResponseDtoActual = searchService.search("testCity", "testPlace");

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
}

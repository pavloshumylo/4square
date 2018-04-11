package com.foursquare.service;

import com.foursquare.dao.SearchDao;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.foursquare.service.service.impl.SearchServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;

public class SearchServiceImplTest {

    @InjectMocks
    private SearchServiceImpl searchService;

    @Mock
    private SearchDao searchDao;

    private String jsonFromDao;
    private SearchResponseDto searchResponseDtoExpected;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        searchResponseDtoExpected = new SearchResponseDto();

        VenueDto venueDtoExpected = new VenueDto();
        venueDtoExpected.setId("535a021d498ed71c77ed20e7");
        venueDtoExpected.setName("Нова пошта (відділення №14)");
        venueDtoExpected.setPhone("+380322901911");
        venueDtoExpected.setAddress("вул. Словацького, 5");
        searchResponseDtoExpected.getVenues().add(venueDtoExpected);

        jsonFromDao = "{\n" +
                "    \"meta\": {\n" +
                "        \"code\": 200,\n" +
                "        \"requestId\": \"5ac5eb54351e3d4df8cebb05\"\n" +
                "    },\n" +
                "    \"response\": {\n" +
                "        \"venues\": [\n" +
                "            {\n" +
                "                \"id\": \"535a021d498ed71c77ed20e7\",\n" +
                "                \"name\": \"Нова пошта (відділення №14)\",\n" +
                "                \"contact\": {\n" +
                "                    \"phone\": \"+380322901911\",\n" +
                "                    \"formattedPhone\": \"+380 322 901 911\",\n" +
                "                    \"twitter\": \"_novaposhta_\"\n" +
                "                },\n" +
                "                \"location\": {\n" +
                "                    \"address\": \"вул. Словацького, 5\",\n" +
                "                    \"crossStreet\": \"вул. Петра Дорошенка\",\n" +
                "                    \"lat\": 49.83832099953952,\n" +
                "                    \"lng\": 24.023289687789234\n" +
                "\t\t\t\t\t}\n" +
                "\t\t\t}\n" +
                "\t\t\t\t\t]\n" +
                "\t}\n" +
                "}";
    }

    @Test
    public void testSearch() {
        when(searchDao.search(any(String.class), any(String.class))).thenReturn(jsonFromDao);
        SearchResponseDto searchResponseDtoActual = searchService.search("testCity", "testPlace");

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

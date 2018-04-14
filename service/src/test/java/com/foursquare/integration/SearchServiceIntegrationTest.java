package com.foursquare.integration;

import com.foursquare.dao.SearchDao;
import com.foursquare.dao.dao.impl.MockSearchDaoImpl;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.foursquare.service.SearchService;
import com.foursquare.service.service.impl.SearchServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class SearchServiceIntegrationTest {

    @Autowired
    private SearchService searchService;

    private SearchResponseDto searchResponseDtoExpected;

    @Before
    public void init() {
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
    }

    @Test
    public void testSearchService_ShouldReturnSearchResponseDto() {
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

    @TestConfiguration
    static class ContextConfiguration {

        @Bean
        public SearchService searchService() {
            return new SearchServiceImpl();
        }

        @Bean
        public SearchDao searchDao(){
            return new MockSearchDaoImpl();
        }
    }
}

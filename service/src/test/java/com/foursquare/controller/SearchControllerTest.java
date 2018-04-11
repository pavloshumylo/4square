package com.foursquare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.foursquare.service.SearchService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SearchControllerTest {

    @InjectMocks
    private SearchController searchController;

    @Mock
    private SearchService searchService;

    private MockMvc mockMvc;
    private ObjectMapper mapper;
    private SearchResponseDto searchResponseDto;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(searchController).build();
        mapper = new ObjectMapper();

        searchResponseDto = new SearchResponseDto();
        VenueDto venueDto = new VenueDto();
            venueDto.setId("1");
            venueDto.setName("TestName 1");
            venueDto.setPhone("TestPhone 1");
            venueDto.setAddress("TestAddress 1");
            searchResponseDto.getVenues().add(venueDto);
        }

    @Test
    public void testSearch_ShouldReturnSearchResponseDto() throws Exception {
        when(searchService.search(any(String.class), any(String.class))).thenReturn(searchResponseDto);
        MvcResult mvcResult = mockMvc.perform(get("/search")
                .param("near", "testCity")
                .param("query", "testPlace"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        SearchResponseDto resultSearchResponseDto = mapper.readValue(responseString, SearchResponseDto.class);

        assertEquals(searchResponseDto.getVenues().get(0).getId(), resultSearchResponseDto.getVenues().get(0).getId());
        assertEquals(searchResponseDto.getVenues().get(0).getName(), resultSearchResponseDto.getVenues().get(0).getName());
        assertEquals(searchResponseDto.getVenues().get(0).getPhone(), resultSearchResponseDto.getVenues().get(0).getPhone());
        assertEquals(searchResponseDto.getVenues().get(0).getAddress(), resultSearchResponseDto.getVenues().get(0).getAddress());
    }
}

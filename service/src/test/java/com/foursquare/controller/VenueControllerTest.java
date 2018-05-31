package com.foursquare.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.entity.Venue;
import com.foursquare.service.VenueService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VenueControllerTest {

    @InjectMocks
    private VenueController venueController;

    @Mock
    private VenueService venueService;

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(venueController).build();
    }

    @Test
    public void testSave_ShouldReturnOkResponseEntityAndShouldInvokeSaveMethodOnce() throws Exception {
        doNothing().when(venueService).save(any(Venue.class));

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("testData/venue_controller_valid_user.json");

        mockMvc.perform(post("/venue/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().readTree(is).toString()))
                .andExpect(status().isOk());

        verify(venueService).save(any(Venue.class));
    }

    @Test
    public void testSave_ShouldReturnBadRequestResponseEntity() throws Exception {
        doNothing().when(venueService).save(any(Venue.class));

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("testData/venue_controller_invalid_user.json");

        mockMvc.perform(post("/venue/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().readTree(is).toString()))
                .andExpect(status().isBadRequest());
        }

     @Test
    public void testRemove_ShouldReturnOkResponseEntityAndShouldInvokeRemoveMethodOnce() throws Exception {
         doNothing().when(venueService).remove(any(Venue.class));

         InputStream is = getClass().getClassLoader()
                 .getResourceAsStream("testData/venue_controller_valid_user.json");

         mockMvc.perform(delete("/venue/remove")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(new ObjectMapper().readTree(is).toString()))
                 .andExpect(status().isOk());

         verify(venueService).remove(any(Venue.class));
     }

     @Test
    public void testRemove_ShouldReturnBadRequestResponseEntity() throws Exception {
        doNothing().when(venueService).remove(any(Venue.class));

         InputStream is = getClass().getClassLoader()
                 .getResourceAsStream("testData/venue_controller_invalid_user.json");

         mockMvc.perform(delete("/venue/remove")
                 .contentType(MediaType.APPLICATION_JSON)
                 .content(new ObjectMapper().readTree(is).toString()))
                 .andExpect(status().isBadRequest());
     }

     @Test
    public void testGet_ShouldReturnListOfProperVenuesAndOkResponseStatus() throws Exception {
        List<Venue> venuesExpected = Arrays.asList(initializeVenue(), initializeVenue());

        when(venueService.get()).thenReturn(venuesExpected);

         MvcResult mvcResult = mockMvc.perform(get("/venue/get/all"))
                 .andExpect(status().isOk())
                 .andReturn();

         String responseString = mvcResult.getResponse().getContentAsString();
         List<Venue> venuesActual = new ObjectMapper().readValue(responseString, new TypeReference<List<Venue>>() {});

         assertEquals(venuesExpected, venuesActual);
         assertEquals(venuesExpected.get(0).getId(), venuesActual.get(0).getId());
         assertEquals(venuesExpected.get(0).getName(), venuesActual.get(0).getName());
         assertEquals(venuesExpected.get(0).getAddress(), venuesActual.get(0).getAddress());
         assertEquals(venuesExpected.get(1).getId(), venuesActual.get(1).getId());
         assertEquals(venuesExpected.get(1).getName(), venuesActual.get(1).getName());
         assertEquals(venuesExpected.get(1).getAddress(), venuesActual.get(1).getAddress());
     }

     private Venue initializeVenue() {
         Venue venue = new Venue();
         venue.setId(1);
         venue.setName("venueName");
         venue.setAddress("venueAddress");
         return venue;
     }
}

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class VenueControllerTest {

    @InjectMocks
    private VenueController venueController;

    @Mock
    private VenueService venueService;

    private MockMvc mockMvc;
    private static final String FS_ID = "4bec2c3062c0c92865ffe2d4";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(venueController).build();
    }

    @Test
    public void testSave_ShouldReturnOkResponseEntityAndShouldInvokeSaveMethodOnce() throws Exception {
        doNothing().when(venueService).save(anyString());

        mockMvc.perform(post("/venues/"+FS_ID))
                .andExpect(status().isOk());

        verify(venueService).save(FS_ID);
    }

     @Test
    public void testRemove_ShouldReturnOkResponseEntityAndShouldInvokeRemoveMethodOnce() throws Exception {
         doNothing().when(venueService).remove(anyString());

         mockMvc.perform(delete("/venues/"+FS_ID))
                 .andExpect(status().isOk());

         verify(venueService).remove(FS_ID);
     }

     @Test
    public void testGet_ShouldReturnListOfProperVenuesAndOkResponseStatus() throws Exception {
        List<Venue> venuesExpected = Arrays.asList(initializeVenue(), initializeVenue());

        when(venueService.getAll()).thenReturn(venuesExpected);

         MvcResult mvcResult = mockMvc.perform(get("/venues"))
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

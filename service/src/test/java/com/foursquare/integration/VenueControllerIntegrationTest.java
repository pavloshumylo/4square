package com.foursquare.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.config.FourSquareProperties;
import com.foursquare.controller.VenueController;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.exception.VenueException;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VenueControllerIntegrationTest {

    private static final String APPLICATION_JSON_VALUE ="application/json;charset=UTF-8";

    @Autowired
    private VenueController venueController;
    @Autowired
    private FourSquareProperties fourSquareProperties;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VenueRepository venueRepository;

    private MockMvc mockMvc;
    private User user;
    private InputStream inputStreamFirst, inputStreamSecond;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Before
    public void init() {
        fourSquareProperties.setApiHost("http://localhost:"+wireMockRule.port()+"/");
        mockMvc = MockMvcBuilders.standaloneSetup(venueController).build();

        user = new User();
        user.setName("user");
        user.setPassword("testPassword@1");
        user.setCity("testCity");
        user.setEmail("email@exmaple.com");

        userRepository.save(user);

        inputStreamFirst = getClass().getClassLoader()
                .getResourceAsStream("testData/venue_controller_integration_test_venue_by_fs_id.json");
        inputStreamSecond = getClass().getClassLoader()
                .getResourceAsStream("testData/venue_controller_valid_user.json");
    }

    @After
    public void cleanup() {
        venueRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles="ADMIN")
    public void testSave_ShouldReturnOkResponseEntity() throws Exception {
        stubFor(WireMock.get(urlMatching("/v2/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(new ObjectMapper().readTree(inputStreamFirst).toString())));

        mockMvc.perform(post("/venue/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().readTree(inputStreamSecond).toString()))
                .andExpect(status().isOk());
        }

    @Test
    @WithMockUser(roles="ADMIN")
    public void testSave_ShouldThrowVenueException() {
            venueRepository.save(initializeVenue());

        try {
            mockMvc.perform(post("/venue/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().readTree(inputStreamSecond).toString()));

            Assert.fail();
        } catch (Exception ex) {
            assertTrue(ex.getCause() instanceof VenueException);
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testRemove_ShouldReturnOkResponseEntity() throws Exception {
        venueRepository.save(initializeVenue());

        mockMvc.perform(delete("/venue/remove")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().readTree(inputStreamSecond).toString()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testRemove_ShouldThrowVenueException() {
        try {
            mockMvc.perform(delete("/venue/remove")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().readTree(inputStreamSecond).toString()));

            Assert.fail();
        } catch (Exception ex) {
            assertTrue(ex.getCause() instanceof VenueException);
        }
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    public void testGet_ShouldReturnListOfProperVenuesAndOkResponseStatus() throws Exception {
        Venue venueFirst = initializeVenue();
        Venue venueSecond = initializeVenue();

        venueRepository.save(venueFirst);
        venueRepository.save(venueSecond);

        MvcResult mvcResult = mockMvc.perform(get("/venue/get/all"))
                .andExpect(status().isOk())
                .andReturn();

        String responseString = mvcResult.getResponse().getContentAsString();
        List<Venue> venuesActual = new ObjectMapper().readValue(responseString, new TypeReference<List<Venue>>() {});

        assertEquals(venueFirst.getId(), venuesActual.get(0).getId());
        assertEquals(venueFirst.getUser(), venuesActual.get(0).getUser());
        assertEquals(venueSecond.getFsId(), venuesActual.get(1).getFsId());
        assertEquals(venueSecond.getAddedAt(), venuesActual.get(1).getAddedAt());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testGet_ShouldThrowVenueException() throws Exception {
        try {
            mockMvc.perform(get("/venue/get/all"))
                    .andExpect(status().isOk())
                    .andReturn();

            Assert.fail();
        } catch (Exception ex) {
            assertTrue(ex.getCause() instanceof VenueException);
        }
    }

    private Venue initializeVenue() {
        Venue venue = new Venue();
        venue.setUser(user);
        venue.setFsId("4bec2c3062c0c92865ffe2d4");
        venue.setAddedAt(new Date());
        return venue;
    }
}
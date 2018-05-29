package com.foursquare.integration;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;
import java.util.Date;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertTrue;
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
    private InputStream inputStreamSecond;

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
    public void testSave_ShouldReturnOkResponse() throws Exception {
        InputStream inputStreamFirst = getClass().getClassLoader()
                .getResourceAsStream("testData/venue_controller_integration_test_venue_by_fs_id.json");

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
        try {
            Venue venue = new Venue();
            venue.setUser(user);
            venue.setFsId("4bec2c3062c0c92865ffe2d4");
            venue.setAddedAt(new Date());

            venueRepository.save(venue);

            mockMvc.perform(post("/venue/save")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().readTree(inputStreamSecond).toString()))
                    .andExpect(status().isOk());

            Assert.fail();
        } catch (Exception ex) {
            assertTrue(ex.getCause() instanceof VenueException);
        }
    }
}
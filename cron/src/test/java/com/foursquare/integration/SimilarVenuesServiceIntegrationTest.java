package com.foursquare.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.config.FourSquareProperties;
import com.foursquare.dto.VenueDto;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.SimilarVenuesService;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SimilarVenuesServiceIntegrationTest {

    private static final String APPLICATION_JSON_VALUE ="application/json;charset=UTF-8";

    @Autowired
    private SimilarVenuesService similarVenuesService;
    @Autowired
    private VenueRepository venueRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FourSquareProperties fourSquareProperties;

    @MockBean
    private JavaMailSender mailSender;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(wireMockConfig().dynamicPort());

    @Test
    public void testEmailSimilarVenues_ShouldInvokeMailSenderOnce() throws IOException {
        fourSquareProperties.setApiHost("http://localhost:"+wireMockRule.port()+"/");

        User user = initializeEntity();
        userRepository.save(user);

        Venue venueFirst = new Venue();
        Venue venueSecond = new Venue();

        venueFirst.setFsId("fsIdFirst");
        venueFirst.setUser(user);

        venueSecond.setFsId("fsIdSecond");
        venueSecond.setUser(user);

        venueRepository.save(Arrays.asList(venueFirst, venueSecond));

        InputStream is = getClass().getClassLoader().getResourceAsStream("testData/similar_dao_response_for_json_node_dao_response.json");
        String responseExpected = new ObjectMapper().readTree(is).toString();

        stubFor(get(urlMatching("/v2/venues/.*"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", APPLICATION_JSON_VALUE)
                        .withBody(responseExpected)));

        similarVenuesService.emailSimilarVenues();

        VenueDto venueDtoExpected = new VenueDto();
        venueDtoExpected.setId("535a021d498ed71c77ed20e6");
        venueDtoExpected.setName("Нова пошта (відділення №14)");
        venueDtoExpected.setAddress("вул. Словацького, 5");

        SimpleMailMessage mailMessageExpected = new SimpleMailMessage();
        mailMessageExpected.setTo(user.getEmail());
        mailMessageExpected.setSubject("Similar venues. User: " + user.getId());
        mailMessageExpected.setText(Arrays.asList(venueDtoExpected).toString());

        verify(mailSender).send(mailMessageExpected);
    }

    private User initializeEntity() {
        User user = new User();
        user.setName("firstUser");
        user.setEmail("userFirst@email.com");
        user.setCity("firstCity");
        user.setPassword("somePassowrd1@");
        return user;
    }
}
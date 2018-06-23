package com.foursquare.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SimilarVenuesDao;
import com.foursquare.dto.VenueDto;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.impl.SimilarVenuesServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class SimilarVenuesServiceImplTest {

    @InjectMocks
    private SimilarVenuesServiceImpl similarVenuesService;

    @Mock
    private SimilarVenuesDao similarVenuesDao;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VenueRepository venueRepository;
    @Mock
    private JavaMailSender mailSender;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEmailSimilarVenues_ShouldInvokeMailSenderOnce() throws IOException {
        User user = initializeEntity();

        Venue venueFirst = new Venue();
        Venue venueSecond = new Venue();

        venueFirst.setId(1);
        venueFirst.setFsId("fsIdFirst");
        venueFirst.setUser(user);

        venueSecond.setId(2);
        venueSecond.setFsId("fsIdSecond");
        venueSecond.setUser(user);

        VenueDto venueDto = new VenueDto();
        venueDto.setId("535a021d498ed71c77ed20e6");
        venueDto.setName("Нова пошта (відділення №14)");
        venueDto.setAddress("вул. Словацького, 5");

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Similar venues. User: " + user.getId());
        mailMessage.setText(Arrays.asList(venueDto).toString());

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/similar_dao_response_for_json_node_dao_response.json");
        JsonNode responseSimilarVenuesFromDao = new ObjectMapper().readTree(is);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(venueRepository.findAlldByUserId(user.getId())).thenReturn(Arrays.asList(venueFirst, venueSecond));
        when(similarVenuesDao.getSimilarVenues(venueFirst.getFsId())).thenReturn(responseSimilarVenuesFromDao);
        when(similarVenuesDao.getSimilarVenues(venueSecond.getFsId())).thenReturn(responseSimilarVenuesFromDao);

        similarVenuesService.emailSimilarVenues();
        verify(mailSender).send(mailMessage);
    }

    @Test
    public void testEmailSimilarVenues_emptyUserList_ShouldInvokeMailSenderNever() {
        similarVenuesService.emailSimilarVenues();
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testEmailSimilarVenues_emptyVenuesList_ShouldInvokeMailSenderNever() {
        User user = initializeEntity();


        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        similarVenuesService.emailSimilarVenues();
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    private User initializeEntity() {
        User user = new User();
        user.setId(1);
        user.setName("firstUser");
        user.setEmail("userFirst@email.com");
        user.setCity("firstCity");
        user.setPassword("somePassowrd1@");
        return user;
    }
}
package com.foursquare.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SimilarVenuesDao;
import com.foursquare.dto.VenueDto;
import com.foursquare.entity.Category;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.impl.EmailNotificationServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailNotificationServiceImplTest {

    @InjectMocks
    private EmailNotificationServiceImpl emailNotificationService;

    @Mock
    private SimilarVenuesDao similarVenuesDao;
    @Mock
    private UserRepository userRepository;
    @Mock
    private VenueRepository venueRepository;
    @Mock
    private JavaMailSender mailSender;

    private Category categoryFirst, categorySecond, categoryThird, categoryFourth, categoryFifth;
    private Venue venueFirst, venueSecond, venueThird, venueFourth, venueFifth;

    @Test
    public void testEmailSimilarVenues_ShouldInvokeMailSenderOnce() throws IOException {
        User user = initializeEntity();

        venueFirst = new Venue();
        venueSecond = new Venue();

        venueFirst.setId(1);
        venueFirst.setFsId("fsIdFirst");
        venueFirst.setUser(user);

        venueSecond.setId(2);
        venueSecond.setFsId("fsIdSecond");
        venueSecond.setUser(user);

        VenueDto venueDtoExpected = new VenueDto();
        venueDtoExpected.setId("535a021d498ed71c77ed20e6");
        venueDtoExpected.setName("Нова пошта (відділення №14)");
        venueDtoExpected.setAddress("вул. Словацького, 5");

        SimpleMailMessage mailMessageExpected = new SimpleMailMessage();
        mailMessageExpected.setTo(user.getEmail());
        mailMessageExpected.setSubject("Similar venues. User: " + user.getId());
        mailMessageExpected.setText(Arrays.asList(venueDtoExpected).toString());

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/similar_dao_response_for_json_node_dao_response.json");
        JsonNode responseSimilarVenuesFromDao = new ObjectMapper().readTree(is);

        when(userRepository.findAll()).thenReturn(Arrays.asList(user));
        when(venueRepository.findAlldByUserId(user.getId())).thenReturn(Arrays.asList(venueFirst, venueSecond));
        when(similarVenuesDao.getSimilarVenues(venueFirst.getFsId())).thenReturn(responseSimilarVenuesFromDao);
        when(similarVenuesDao.getSimilarVenues(venueSecond.getFsId())).thenReturn(responseSimilarVenuesFromDao);

        emailNotificationService.emailSimilarVenues();
        verify(mailSender).send(mailMessageExpected);
    }

    @Test
    public void testEmailSimilarVenues_emptyUserList_ShouldInvokeMailSenderNever() {
        emailNotificationService.emailSimilarVenues();
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testEmailSimilarVenues_emptyVenuesList_ShouldInvokeMailSenderNever() {
        User user = initializeEntity();

        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        emailNotificationService.emailSimilarVenues();
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testEmailTrendingCategories_ShouldInvokeMailSenderWithMessageTrendingThreeCategories() {
        categoryFirst = initializeCategory("categoryFirstName");
        categorySecond = initializeCategory("categorySecondName");
        categoryThird = initializeCategory("categoryThirdName");
        categoryFourth = initializeCategory("categoryFourthName");
        categoryFifth = initializeCategory("categoryFifthName");

        venueFirst = initializeVenue((Arrays.asList(categoryThird, categoryFifth)));
        venueSecond = initializeVenue(Arrays.asList(categoryThird, categorySecond, categoryFifth));
        venueThird = initializeVenue(Arrays.asList(categoryFirst, categoryFourth));
        venueFourth = initializeVenue(Arrays.asList(categoryThird, categoryFifth));
        venueFifth = initializeVenue(Arrays.asList(categorySecond, categoryFourth, categoryFifth));

        User user = initializeEntity();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Trending monthly categories");

        List<String> messagesToEmail = Arrays.asList(
                        "Hello, dear! Check our trending categories",
                        "category: " + categoryFifth.getName() + ", quantity: 4",
                        "category: " + categoryThird.getName() + ", quantity: 3",
                        "category: " + categoryFourth.getName() + ", quantity: 2");
        mailMessage.setText(messagesToEmail.toString());

        when(venueRepository.findAllByAddedAtGreaterThanEqual(any(Date.class))).thenReturn(Arrays.asList(venueFirst,
                venueSecond, venueThird, venueFourth, venueFifth));
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        emailNotificationService.emailTrendingCategories();

        verify(mailSender).send(mailMessage);
    }

    @Test
    public void testEmailTrendingCategories_ShouldInvokeMailSenderWithMessageTrendingTwoCategories() {
        categoryFirst = initializeCategory("categoryFirstName");
        categorySecond = initializeCategory("categorySecondName");

        venueFirst = initializeVenue(Arrays.asList(categorySecond));
        venueSecond = initializeVenue(Arrays.asList(categoryFirst, categorySecond));

        User user = initializeEntity();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Trending monthly categories");

        List<String> messageToEmail = Arrays.asList(
                "Hello, dear! Check our trending categories",
                "category: " + categorySecond.getName() + ", quantity: 2",
                "category: " + categoryFirst.getName() + ", quantity: 1");

        mailMessage.setText(messageToEmail.toString());

        when(venueRepository.findAllByAddedAtGreaterThanEqual(any(Date.class))).thenReturn(Arrays.asList(venueFirst,
                venueSecond));
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        emailNotificationService.emailTrendingCategories();

        verify(mailSender).send(mailMessage);
    }

    @Test
    public void testEmailTrendingCategories_emptyVenuesList_ShouldInvokeMailSenderNever() {
        emailNotificationService.emailTrendingCategories();
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testEmailTrendingCategories_emptyUserList_ShouldInvokeMailSenderNever() {
        categoryFirst = initializeCategory("categoryFirstName");
        categorySecond = initializeCategory("categorySecondName");

        venueFirst = initializeVenue(Arrays.asList(categorySecond));
        venueSecond = initializeVenue(Arrays.asList(categoryFirst, categorySecond));

        when(venueRepository.findAllByAddedAtGreaterThanEqual(any(Date.class))).thenReturn(Arrays.asList(venueFirst,
                venueSecond));

        emailNotificationService.emailTrendingCategories();
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    private Category initializeCategory(String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        return category;
    }

    private Venue initializeVenue(List<Category> venueCategories) {
        Venue venue = new Venue();
        venue.setCategories(venueCategories);
        return venue;
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
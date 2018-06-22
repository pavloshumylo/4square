package com.foursquare.service;

import com.foursquare.entity.Category;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.impl.TrendingCategoriesServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TrendingCategoriesServiceImplTest {

    @InjectMocks
    private TrendingCategoriesServiceImpl trendingCategoriesService;

    @Mock
    private VenueRepository venueRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JavaMailSender mailSender;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testEmailTrendingCategories_ShouldInvokeMailSenderWithMessageTopThreeCategories() {
        Category categoryFirst = new Category();
        Category categorySecond = new Category();
        Category categoryThird = new Category();
        Category categoryFourth = new Category();
        Category categoryFifth = new Category();

        categoryFirst.setId(1);
        categoryFirst.setFsId("firstFsId");
        categoryFirst.setName("firstCategoryName");

        categorySecond.setId(2);
        categorySecond.setFsId("secondFsId");
        categorySecond.setName("secondCategoryName");

        categoryThird.setId(3);
        categoryThird.setFsId("thirdFsId");
        categoryThird.setName("thirdCategoryName");

        categoryFourth.setId(4);
        categoryFourth.setFsId("fourthFsId");
        categoryFourth.setName("fourthCategoryName");

        categoryFifth.setId(5);
        categoryFifth.setFsId("fifthFsId");
        categoryFifth.setName("fifthCategoryName");

        Venue venueFirst = new Venue();
        Venue venueSecond = new Venue();
        Venue venueThird = new Venue();
        Venue venueFourth = new Venue();
        Venue venueFifth = new Venue();

        venueFirst.setId(1);
        venueFirst.setFsId("fsIdFirst");
        venueFirst.setCategories(Arrays.asList(categoryThird, categoryFifth));

        venueSecond.setId(2);
        venueSecond.setFsId("fsIdSecond");
        venueSecond.setCategories(Arrays.asList(categoryThird, categorySecond, categoryFifth));

        venueThird.setId(3);
        venueThird.setFsId("fsIdSThird");
        venueThird.setCategories(Arrays.asList(categoryFirst, categoryFourth));

        venueFourth.setId(4);
        venueFourth.setFsId("fsIdFourth");
        venueFourth.setCategories(Arrays.asList(categoryThird, categoryFifth));

        venueFifth.setId(5);
        venueFifth.setFsId("fsIdFifth");
        venueFifth.setCategories(Arrays.asList(categorySecond, categoryFourth, categoryFifth));

        User user = initializeEntity();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Top monthly categories");

        List<String> messagesToEmail = Arrays.asList(
                "Category: " + categoryFifth.getName() + "; Quantity: 4",
                "Category: " + categoryThird.getName() + "; Quantity: 3",
                "Category: " + categorySecond.getName() + "; Quantity: 2",
                "Category: " + categoryFourth.getName() + "; Quantity: 2");

        mailMessage.setText(messagesToEmail.toString());

        when(venueRepository.findAllByAddedAtGreaterThanEqual(any(Date.class))).thenReturn(Arrays.asList(venueFirst,
                venueSecond, venueThird, venueFourth, venueFifth));
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        trendingCategoriesService.emailTrendingCategories();

        verify(mailSender).send(mailMessage);
    }

    @Test
    public void testEmailTrendingCategories_ShouldInvokeMailSenderWithMessageTopOneCategory() {
        Category categoryFirst = new Category();
        Category categorySecond = new Category();

        categoryFirst.setId(1);
        categoryFirst.setFsId("firstFsId");
        categoryFirst.setName("firstCategoryName");

        categorySecond.setId(2);
        categorySecond.setFsId("secondFsId");
        categorySecond.setName("secondCategoryName");

        Venue venueFirst = new Venue();
        Venue venueSecond = new Venue();

        venueFirst.setId(1);
        venueFirst.setFsId("fsIdFirst");
        venueFirst.setCategories(Arrays.asList(categorySecond));

        venueSecond.setId(2);
        venueSecond.setFsId("fsIdSecond");
        venueSecond.setCategories(Arrays.asList(categoryFirst, categorySecond));

        User user = initializeEntity();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Top monthly categories");

        List<String> messageToEmail = Arrays.asList("Category: " + categorySecond.getName() + "; Quantity: 2");

        mailMessage.setText(messageToEmail.toString());

        when(venueRepository.findAllByAddedAtGreaterThanEqual(any(Date.class))).thenReturn(Arrays.asList(venueFirst,
                venueSecond));
        when(userRepository.findAll()).thenReturn(Arrays.asList(user));

        trendingCategoriesService.emailTrendingCategories();

        verify(mailSender).send(mailMessage);
    }

    @Test
    public void testEmailTrendingCategories_emptyUserList_ShouldInvokeMailSenderNever() {
        Category categoryFirst = new Category();
        Category categorySecond = new Category();

        categoryFirst.setId(1);
        categoryFirst.setFsId("firstFsId");
        categoryFirst.setName("firstCategoryName");

        categorySecond.setId(2);
        categorySecond.setFsId("secondFsId");
        categorySecond.setName("secondCategoryName");

        Venue venueFirst = new Venue();
        Venue venueSecond = new Venue();

        venueFirst.setId(1);
        venueFirst.setFsId("fsIdFirst");
        venueFirst.setCategories(Arrays.asList(categorySecond));

        venueSecond.setId(2);
        venueSecond.setFsId("fsIdSecond");
        venueSecond.setCategories(Arrays.asList(categoryFirst, categorySecond));

        User user = initializeEntity();

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(user.getEmail());
        mailMessage.setSubject("Top monthly categories");

        List<String> messageToEmail = Arrays.asList("Category: " + categorySecond.getName() + "; Quantity: 2");

        mailMessage.setText(messageToEmail.toString());

        when(venueRepository.findAllByAddedAtGreaterThanEqual(any(Date.class))).thenReturn(Arrays.asList(venueFirst,
                venueSecond));

        trendingCategoriesService.emailTrendingCategories();
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
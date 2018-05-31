package com.foursquare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import com.foursquare.entity.Category;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.exception.VenueException;
import com.foursquare.repository.CategoryRepository;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.impl.VenueServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest(SecurityContextHolder.class)
public class VenueServiceImplTest {

    @InjectMocks
    private VenueServiceImpl venueService;

    @Mock
    private VenueRepository venueRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private SearchDao searchDao;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    private User user;
    private Venue venue;
    private InputStream inputStream;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setId(1);

        venue = new Venue();
        venue.setFsId("4bec2c3062c0c92865ffe2d4");

        PowerMockito.mockStatic(SecurityContextHolder.class);
        when(SecurityContextHolder.getContext()).thenReturn(securityContext);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("Pavlo");
        when(userRepository.findByName(anyString())).thenReturn(user);
    }

    @Test
    public void testSave_ShouldInvokeSaveOnce() throws IOException {
        inputStream = getClass().getClassLoader()
                .getResourceAsStream("testData/venue_service_venue_by_fs_id.json");

        when(venueRepository.findByUserIdAndFsId(anyInt(), anyString())).thenReturn(null);
        when(searchDao.search(anyString())).thenReturn(new ObjectMapper().readTree(inputStream));
        when(categoryRepository.findByFsId(anyString())).thenReturn(null);
        when(categoryRepository.save(any(Category.class))).thenReturn(null);

        venueService.save(venue);
        verify(venueRepository).save(any(Venue.class));
    }

    @Test
    public void testSave_ShouldThrowVenueExceptionWithMessageCategoryDoesntExist() throws IOException {
        inputStream = getClass().getClassLoader()
                .getResourceAsStream("testData/venue_service_venue_without_category_id.json");

        when(venueRepository.findByUserIdAndFsId(anyInt(), anyString())).thenReturn(null);
        when(searchDao.search(anyString())).thenReturn(new ObjectMapper().readTree(inputStream));

        try {
            venueService.save(venue);

            Assert.fail();
        } catch (VenueException ex) {
            assertEquals("Category foursquare id doesn't exist", ex.getMessage());
        }
    }

    @Test
    public void testSave_ShouldThrowVenueExceptionWithMessageCannotValidateVenue() throws IOException {
        inputStream = getClass().getClassLoader()
                .getResourceAsStream("testData/venue_service_venue_without_id.json");

        when(venueRepository.findByUserIdAndFsId(anyInt(), anyString())).thenReturn(null);
        when(searchDao.search(anyString())).thenReturn(new ObjectMapper().readTree(inputStream));

        try {
            venueService.save(venue);

            Assert.fail();
        } catch (VenueException ex) {
            assertEquals("Cannot validate venue.", ex.getMessage());
        }
    }

    @Test(expected = VenueException.class)
    public void testSave_ShouldThrowVenueException() {
        when(venueRepository.findByUserIdAndFsId(1, "4bec2c3062c0c92865ffe2d4")).thenReturn(venue);

        venueService.save(venue);
    }

    @Test
    public void testRemove_ShouldInvokeDeleteOnce() {
        when(venueRepository.findByUserIdAndFsId(1, "4bec2c3062c0c92865ffe2d4")).thenReturn(venue);

        venueService.remove(venue);

        verify(venueRepository).delete(0);
    }

    @Test(expected = VenueException.class)
    public void testRemove_ShouldThrowVenueException() {
        when(venueRepository.findByUserIdAndFsId(anyInt(), anyString())).thenReturn(null);

        venueService.remove(venue);
    }

    @Test
    public void testGet_ShouldReturnListOfProperVenues() {
        List<Venue> venuesExpected = Arrays.asList(initializeVenue(), initializeVenue());
        when(venueRepository.findAlldByUserId(anyInt())).thenReturn(venuesExpected);

        List<Venue> venuesActual = venueService.get();

        assertEquals(venuesExpected, venuesActual);
        assertEquals(venuesExpected.get(0).getId(), venuesActual.get(0).getId());
        assertEquals(venuesExpected.get(0).getName(), venuesActual.get(0).getName());
        assertEquals(venuesExpected.get(0).getAddress(), venuesActual.get(0).getAddress());
        assertEquals(venuesExpected.get(1).getId(), venuesActual.get(1).getId());
        assertEquals(venuesExpected.get(1).getName(), venuesActual.get(1).getName());
        assertEquals(venuesExpected.get(1).getAddress(), venuesActual.get(1).getAddress());
    }

    @Test(expected = VenueException.class)
    public void testGet_ShouldThrowVenueException() {
        when(venueRepository.findAlldByUserId(anyInt())).thenReturn(new ArrayList<>());

        venueService.get();
    }

    private Venue initializeVenue() {
        Venue venue = new Venue();
        venue.setId(1);
        venue.setName("venueName");
        venue.setAddress("venueAddress");
        return venue;
    }
}
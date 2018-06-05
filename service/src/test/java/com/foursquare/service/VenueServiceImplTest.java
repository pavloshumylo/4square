package com.foursquare.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import com.foursquare.entity.Category;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.exception.BadRequestException;
import com.foursquare.exception.ResourceNotFoundException;
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
    private static final String FS_ID = "4bec2c3062c0c92865ffe2d4";

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        user = new User();
        user.setId(1);

        venue = new Venue();
        venue.setId(1);
        venue.setName("venueName");
        venue.setAddress("venueAddress");
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

        venueService.save(FS_ID);
        verify(venueRepository).save(any(Venue.class));
    }

    @Test
    public void testSave_ShouldThrowBadRequestExceptionWithMessageInvalidVenue() throws IOException {
        inputStream = getClass().getClassLoader()
                .getResourceAsStream("testData/venue_service_venue_without_id.json");

        when(venueRepository.findByUserIdAndFsId(anyInt(), anyString())).thenReturn(null);
        when(searchDao.search(FS_ID)).thenReturn(new ObjectMapper().readTree(inputStream));

        try {
            venueService.save(FS_ID);

            Assert.fail();
        } catch (BadRequestException ex) {
            assertEquals("Invalid venue.", ex.getMessage());
        }
    }

    @Test
    public void testSave_ShouldThrowBadRequestExceptionWithMessageResponseDoesntHaveVenue() {
        when(venueRepository.findByUserIdAndFsId(anyInt(), anyString())).thenReturn(null);
        when(searchDao.search(anyString())).thenReturn(null);

        try {
            venueService.save(FS_ID);

            Assert.fail();
        } catch (BadRequestException ex) {
            assertEquals("JsonNode from api doesn't have venue.", ex.getMessage());
        }
    }

    @Test
    public void testSave_ShouldThrowBadRequestException() {
        when(venueRepository.findByUserIdAndFsId(user.getId(), FS_ID)).thenReturn(venue);

        try {
            venueService.save(FS_ID);

            Assert.fail();
        } catch (BadRequestException ex) {
            assertEquals("Unable to create venue with id: " + FS_ID + " Venue exists already.", ex.getMessage());
        }
    }

    @Test
    public void testRemove_ShouldInvokeDeleteOnce() {
        when(venueRepository.findByUserIdAndFsId(user.getId(), FS_ID)).thenReturn(venue);

        venueService.remove(FS_ID);

        verify(venueRepository).delete(venue.getId());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void testRemove_ShouldThrowResourceNotFoundException() {
        when(venueRepository.findByUserIdAndFsId(anyInt(), anyString())).thenReturn(null);

        venueService.remove(FS_ID);
    }

    @Test
    public void testGet_ShouldReturnListOfProperVenues() {
        List<Venue> venuesExpected = Arrays.asList(venue, venue);
        when(venueRepository.findAlldByUserId(anyInt())).thenReturn(venuesExpected);

        List<Venue> venuesActual = venueService.getAll();

        assertEquals(venuesExpected, venuesActual);
        assertEquals(venuesExpected.get(0).getId(), venuesActual.get(0).getId());
        assertEquals(venuesExpected.get(0).getName(), venuesActual.get(0).getName());
        assertEquals(venuesExpected.get(0).getAddress(), venuesActual.get(0).getAddress());
        assertEquals(venuesExpected.get(1).getId(), venuesActual.get(1).getId());
        assertEquals(venuesExpected.get(1).getName(), venuesActual.get(1).getName());
        assertEquals(venuesExpected.get(1).getAddress(), venuesActual.get(1).getAddress());
    }
}
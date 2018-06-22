package com.foursquare.repository;

import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class VenueRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserRepository userRepository;

    private Venue firstVenueExpected, secondVenueExpected;
    private static User user;

    @BeforeClass
    public static void initCommon() {
        user = new User();
        user.setName("userName");
        user.setPassword("somePassword@1");
        user.setCity("Lviv");
        user.setEmail("email@exmaple.com");
    }

    @Before
    public void init() {
        user = userRepository.save(user);
        firstVenueExpected = initializeEntity();
        secondVenueExpected = initializeEntity();
    }

    @Test
    public void testSave_ShouldSaveAndReturnExpectedVenue() {
        Venue firstVenueActual = venueRepository.save(firstVenueExpected);
        Venue secondVenueActual = venueRepository.save(secondVenueExpected);
        long countActual = venueRepository.count();

        assertEquals(firstVenueExpected, firstVenueActual);
        assertEquals(secondVenueExpected, secondVenueActual);
        assertEquals(2, countActual);
    }

    @Test
    public void testGet_ShouldReturnProperVenueById() {
        entityManager.persist(firstVenueExpected);
        entityManager.persist(secondVenueExpected);

        Venue firstVenueActual = venueRepository.getOne(firstVenueExpected.getId());
        Venue secondVenueActual = venueRepository.getOne(secondVenueExpected.getId());

        assertEquals(firstVenueExpected, firstVenueActual);
        assertEquals(secondVenueExpected, secondVenueActual);
    }

    @Test
    public void testFindAll_ShouldReturnAllVenues() {
        entityManager.persist(firstVenueExpected);
        entityManager.persist(secondVenueExpected);

        List<Venue> venuesExpected = venueRepository.findAll();

        assertEquals(venuesExpected, Arrays.asList(firstVenueExpected, secondVenueExpected));
    }

    @Test
    public void testDelete_ShouldDeleteProperVenue() {
        entityManager.persist(firstVenueExpected);
        entityManager.persist(secondVenueExpected);

        venueRepository.delete(firstVenueExpected);
        venueRepository.delete(secondVenueExpected);
        assertThat(venueRepository.findAll()).isEmpty();
    }

    @Test
    public void testFindAllByUserId_ShouldReturnProperListOfVenues() {
        entityManager.persist(firstVenueExpected);
        entityManager.persist(secondVenueExpected);

        List<Venue> venuesActual = venueRepository.findAlldByUserId(user.getId());
        assertEquals(Arrays.asList(firstVenueExpected, secondVenueExpected), venuesActual);
    }

    @Test
    public void testFindAllByUserId_notExistingVenuesWithProperUserId_ShouldReturnEmptyList() {
        List<Venue> venuesActual = venueRepository.findAlldByUserId(user.getId());

        assertTrue(venuesActual.isEmpty());
    }

    @Test
    public void testFindAllByAddedAtGreaterThanEqual_ShouldReturnListOfProperVenues() {
        entityManager.persist(firstVenueExpected);
        entityManager.persist(secondVenueExpected);

        LocalDateTime currentDateTime = LocalDateTime.now();

        LocalDateTime beforeCurrentDateTime = LocalDateTime.of(currentDateTime.getYear() - 1, currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(),
                currentDateTime.getHour(), currentDateTime.getMinute(), currentDateTime.getSecond());

        List<Venue> venuesActual = venueRepository.findAllByAddedAtGreaterThanEqual(Date.from(beforeCurrentDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertEquals(Arrays.asList(firstVenueExpected, secondVenueExpected), venuesActual);
    }

    @Test
    public void testFindAllByAddedAtGreaterThanEqual_notExistingVenuesWithAddedAtGreaterThat_ShouldReturnEmptyList() {
        entityManager.persist(firstVenueExpected);
        entityManager.persist(secondVenueExpected);

        LocalDateTime currentDateTime = LocalDateTime.now();

        LocalDateTime afterCurrentDateTime = LocalDateTime.of(currentDateTime.getYear() + 1, currentDateTime.getMonthValue(), currentDateTime.getDayOfMonth(),
                currentDateTime.getHour(), currentDateTime.getMinute(), currentDateTime.getSecond());

        List<Venue> venuesActual = venueRepository.findAllByAddedAtGreaterThanEqual(Date.from(afterCurrentDateTime.atZone(ZoneId.systemDefault()).toInstant()));
        assertTrue(venuesActual.isEmpty());
    }

    private Venue initializeEntity() {
        Venue venue = new Venue();
        venue.setUser(user);
        venue.setFsId("fourSquareId");
        venue.setAddedAt(new Date());
        venue.setName("venueName");
        venue.setAddress("venueAddress");
        venue.setPhone("venuePhone");
        return venue;
    }
}
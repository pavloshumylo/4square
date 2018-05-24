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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;

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
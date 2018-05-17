package com.foursquare.repository;

import com.foursquare.entity.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private User firstUserExpected, secondUserExpected;

    @Before
    public void init() {
        firstUserExpected = new User();
        secondUserExpected = new User();

        firstUserExpected.setName("userName");
        firstUserExpected.setPassword("somePassword@1");
        firstUserExpected.setCity("Lviv");
        firstUserExpected.setEmail("email@exmaple.com");

        secondUserExpected.setName("userName");
        secondUserExpected.setPassword("somePassword@1");
        secondUserExpected.setCity("Lviv");
        secondUserExpected.setEmail("email@exmaple.com");
    }

    @Test
    public void testSave_ShouldSaveAndReturnExpectedUser() {
        User firstUserActual = userRepository.save(firstUserExpected);
        User secondUserActual = userRepository.save(secondUserExpected);
        long countActual = userRepository.count();

        assertEquals(firstUserExpected, firstUserActual);
        assertEquals(secondUserExpected, secondUserActual);
        assertEquals(2, countActual);
    }

    @Test
    public void testGet_ShouldReturnProperUserById() {
        entityManager.persist(firstUserExpected);
        entityManager.persist(secondUserExpected);

        User firstUserActual = userRepository.getOne(firstUserExpected.getId());
        User secondUserActual = userRepository.getOne(secondUserExpected.getId());

        assertEquals(firstUserExpected, firstUserActual);
        assertEquals(secondUserExpected, secondUserActual);
    }

    @Test
    public void testFindAll_ShouldReturnAllUsers() {
        entityManager.persist(firstUserExpected);
        entityManager.persist(secondUserExpected);

        List<User> usersExpected = userRepository.findAll();

        assertEquals(usersExpected, Arrays.asList(firstUserExpected, secondUserExpected));
    }

    @Test
    public void testDelete_ShouldDeleteProperUser() {
        entityManager.persist(firstUserExpected);
        entityManager.persist(secondUserExpected);

        userRepository.delete(firstUserExpected);
        userRepository.delete(secondUserExpected);
        assertThat(userRepository.findAll()).isEmpty();
    }
}
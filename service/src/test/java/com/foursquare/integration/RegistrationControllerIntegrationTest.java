package com.foursquare.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.controller.RegistrationController;
import com.foursquare.entity.User;
import com.foursquare.exception.UserExistException;
import com.foursquare.repository.UserRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationControllerIntegrationTest {

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private MockMvc mockMvc;
    private InputStream inputStream;

    @Before
    public void init() {
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        inputStream = getClass().getClassLoader()
                .getResourceAsStream("testData/registration_controller_user_for_registration.json");
    }

    @After
    public void cleanup() {
        userRepository.deleteAll();
    }

    @Test
    public void testRegistration_ShouldReturnOkResponseEntity() throws Exception {
        user = new User();
        user.setName("testuser");
        user.setPassword("testPassword@1");
        user.setCity("testCity");
        user.setEmail("email@exmaple.com");

        userRepository.save(user);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().readTree(inputStream).toString()))
                .andExpect(status().isOk());
    }

    @Test
    public void testRegistration_ShouldThrowUserExistException() {
        try {
            user = new User();
            user.setName("pavloshumylo");
            user.setPassword("testPassword@1");
            user.setCity("testCity");
            user.setEmail("email@exmaple.com");

            userRepository.save(user);

            mockMvc.perform(post("/registration")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(new ObjectMapper().readTree(inputStream).toString()))
                    .andExpect(status().isOk());

            Assert.fail();
        } catch (Exception ex) {
            assertTrue(ex.getCause() instanceof UserExistException);
        }
    }
}
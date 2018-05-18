package com.foursquare.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.controller.RegistrationController;
import com.foursquare.entity.User;
import com.foursquare.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegistrationControllerIntegrationTest {

    @Autowired
    private RegistrationController registrationController;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testRegistration_ShouldReturnOkResponseEntity() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("testData/registration_controller_user_for_registration.json");

        User user = new User();
        user.setName("testUser");
        user.setPassword("testPassword@1");
        user.setCity("testCity");
        user.setEmail("email@exmaple.com");

        userRepository.save(user);

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().readTree(is).toString()))
                .andExpect(status().isOk());
    }
}
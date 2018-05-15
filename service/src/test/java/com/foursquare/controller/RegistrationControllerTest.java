package com.foursquare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.entity.User;
import com.foursquare.service.RegistrationService;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationControllerTest {

    @InjectMocks
    private RegistrationController registrationController;

    @Mock
    private RegistrationService registrationService;

    @Test
    public void testRegistration_ShouldReturnCreatedResponseEntity() throws Exception {
        MockitoAnnotations.initMocks(this);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
        when(registrationService.register(any(User.class))).thenReturn(new ResponseEntity<>(HttpStatus.CREATED));

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream is = classLoader.getResourceAsStream("testData/registration_controller_user_for_registration.json");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().readTree(is).toString()))
                .andExpect(status().isCreated());

        verify(registrationService).register(any(User.class));
    }
}
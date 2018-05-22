package com.foursquare.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.entity.User;
import com.foursquare.service.RegistrationService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
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

    private MockMvc mockMvc;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    public void testRegistration_ShouldReturnOkResponseEntityAndShouldInvokeRegisterMethodOnce() throws Exception {
        doNothing().when(registrationService).register(any(User.class));

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("testData/registration_controller_user_for_registration.json");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().readTree(is).toString()))
                .andExpect(status().isOk());

        verify(registrationService).register(any(User.class));
    }

    @Test
    public void testRegistration_ShouldReturnBadRequestResponseEntity() throws Exception {
        doNothing().when(registrationService).register(any(User.class));

        InputStream is = getClass().getClassLoader()
                .getResourceAsStream("testData/registration_controller_invalid_user_for_registration.json");

        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().readTree(is).toString()))
                .andExpect(status().isBadRequest());
    }
}
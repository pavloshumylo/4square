package com.foursquare.service;

import com.foursquare.entity.User;
import com.foursquare.repository.UserRepository;
import com.foursquare.service.impl.UserDetailsServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertEquals;

public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadUserByUserName_ShouldThrowUsernameNotFoundException() {
        when(userRepository.findByName(anyString())).thenReturn(user);

        userDetailsService.loadUserByUsername("testName");
    }

    @Test
    public void testLoadUserByUserName_ShouldReturnUserDetailsImpl() {
        user = new User();
        user.setName("testName");
        user.setPassword("testPassword");

        when(userRepository.findByName(anyString())).thenReturn(user);

        UserDetails userDetailsExpected = new org.springframework.security.core.userdetails.
                User("testName", "testPassword", Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

        UserDetails userDetailsActual = userDetailsService.loadUserByUsername("testName");

        assertEquals(userDetailsExpected, userDetailsActual);
    }
}
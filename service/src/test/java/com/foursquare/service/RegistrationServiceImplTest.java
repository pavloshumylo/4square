package com.foursquare.service;

import com.foursquare.entity.User;
import com.foursquare.exception.UserExistException;
import com.foursquare.repository.UserRepository;
import com.foursquare.service.impl.RegistrationServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private UserRepository userRepository;

    private User userFirst;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        userFirst = new User();
    }

    @Test
    public void testRegister_ShouldInvokeSaveMethodOnce() {
        when(userRepository.findByName(anyString())).thenReturn(null);

        registrationService.register(userFirst);

        verify(userRepository).save(userFirst);
    }

    @Test(expected = UserExistException.class)
    public void testRegister_ShouldThrowUserExistException() {
        when(userRepository.findByName(anyString())).thenReturn(userFirst);

        registrationService.register(userFirst);
    }
}
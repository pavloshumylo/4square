package com.foursquare.service.impl;

import com.foursquare.entity.User;
import com.foursquare.repository.UserRepository;
import com.foursquare.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    public void register(User user) {
        userRepository.save(user);
    }
}
package com.foursquare.service.impl;

import com.foursquare.entity.User;
import com.foursquare.exception.UserExistException;
import com.foursquare.repository.UserRepository;
import com.foursquare.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    public void register(User user) {
        if (userRepository.findByName(user.getName()) == null) {
            Optional.ofNullable(user.getPassword()).
                    ifPresent(password -> user.setPassword(new BCryptPasswordEncoder().encode(password)));
            userRepository.save(user);
        } else {
            throw new UserExistException("User already exist with name: " + user.getName());
        }
    }
}
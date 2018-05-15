package com.foursquare.service.impl;

import com.foursquare.entity.User;
import com.foursquare.repository.UserRepository;
import com.foursquare.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegistrationServiceImpl implements RegistrationService {

    @Autowired
    private UserRepository userRepository;

    public ResponseEntity<Void> register(User user) {
        if (userRepository.findByName(user.getName()) == null) {
            Optional.ofNullable(user.getPassword()).
                    ifPresent(password -> user.setPassword(new BCryptPasswordEncoder().encode(password)));
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FOUND);
        }
    }
}
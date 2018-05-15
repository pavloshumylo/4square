package com.foursquare.service;

import com.foursquare.entity.User;
import org.springframework.http.ResponseEntity;

public interface RegistrationService {

    ResponseEntity<Void> register(User user);
}
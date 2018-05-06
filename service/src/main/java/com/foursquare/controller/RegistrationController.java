package com.foursquare.controller;

import com.foursquare.entity.User;
import com.foursquare.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @PostMapping(value = "/registration")
    public ResponseEntity<Void> registration(@RequestBody User user) {
        registrationService.register(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
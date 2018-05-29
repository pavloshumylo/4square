package com.foursquare.controller;

import com.foursquare.entity.Venue;
import com.foursquare.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/venue")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @PostMapping(value = "/save")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> save(@Valid @RequestBody Venue venue) {
        venueService.save(venue);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/remove")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> remove(@Valid @RequestBody Venue venue) {
        venueService.remove(venue);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
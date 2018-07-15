package com.foursquare.controller;

import com.foursquare.entity.Venue;
import com.foursquare.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/venues")
public class VenueController {

    @Autowired
    private VenueService venueService;

    @PostMapping(value = "/{fsId}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> save(@PathVariable String fsId) {
        venueService.save(fsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/{fsId}")
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public ResponseEntity<Void> remove(@PathVariable String fsId) {
        venueService.remove(fsId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @PreAuthorize(value = "hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
    public List<Venue> get() {
        return venueService.getAll();
    }
}
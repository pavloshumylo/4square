package com.foursquare.service.impl;

import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import com.foursquare.exception.VenueException;
import com.foursquare.repository.UserRepository;
import com.foursquare.repository.VenueRepository;
import com.foursquare.service.VenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class VenueServiceImpl implements VenueService {

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private UserRepository userRepository;

    public void save(Venue venue) {
        User user = getCurrentUser();
        if(venueRepository.findByUserIdAndFsId(user.getId(), venue.getFsId()) == null) {
            venue.setUser(user);
            venue.setAddedAt(new Date());
            venueRepository.save(venue);
        } else {
            throw new VenueException("No update allowed. Venue already exist with fs id: " + venue.getFsId());
        }
    }

    public void remove(Venue venue) {
        User user = getCurrentUser();
        Venue venueFound = venueRepository.findByUserIdAndFsId(user.getId(), venue.getFsId());
        if(venueFound != null) {
            venueRepository.delete(venueFound.getId());
        } else {
            throw new VenueException("No delete allowed. Venue doesn't exist with fs id: " + venue.getFsId());
        }
    }

    private User getCurrentUser() {
        return userRepository.findByName(SecurityContextHolder.getContext().getAuthentication().getName());
    }
}
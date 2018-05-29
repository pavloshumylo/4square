package com.foursquare.service;

import com.foursquare.entity.Venue;

public interface VenueService {

    void save(Venue venue);
    void remove(Venue venue);
}
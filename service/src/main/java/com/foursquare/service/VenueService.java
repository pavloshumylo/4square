package com.foursquare.service;

import com.foursquare.entity.Venue;

import java.util.List;

public interface VenueService {

    void save(Venue venue);
    void remove(Venue venue);
    List<Venue> get();
}
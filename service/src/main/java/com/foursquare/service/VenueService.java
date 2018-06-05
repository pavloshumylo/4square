package com.foursquare.service;

import com.foursquare.entity.Venue;

import java.util.List;

public interface VenueService {

    void save(String fsId);
    void remove(String fsId);
    List<Venue> getAll();
}
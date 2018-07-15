package com.foursquare.repository;

import com.foursquare.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Integer> {

    List<Venue> findAlldByUserId(int userId);
    List<Venue> findAllByAddedAtGreaterThanEqual(Date dateFirst);
}
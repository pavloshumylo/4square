package com.foursquare.repository;

import com.foursquare.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VenueRepository extends JpaRepository<Venue, Integer> {

    Venue findByUserIdAndFsId(int userId, String fsId);
}
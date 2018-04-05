package com.dto;

import org.springframework.stereotype.Repository;

import java.util.LinkedList;
import java.util.List;

@Repository
public class SearchResponseDto {

    private List<VenueDto> venues;

    public SearchResponseDto() {
        this.venues = new LinkedList<>();
    }

    public List<VenueDto> getVenues() {
        return venues;
    }

    public void setVenues(List<VenueDto> venues) {
        this.venues = venues;
    }
}

package com.foursquare.dto;

import com.google.common.base.Objects;

import java.util.ArrayList;
import java.util.List;

public class SearchResponseDto {

    private List<VenueDto> venues;

    public SearchResponseDto() {
        this.venues = new ArrayList<>();
    }

    public List<VenueDto> getVenues() {
        return venues;
    }

    public void setVenues(List<VenueDto> venues) {
        this.venues = venues;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchResponseDto that = (SearchResponseDto) o;
        return Objects.equal(venues, that.venues);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(venues);
    }
}

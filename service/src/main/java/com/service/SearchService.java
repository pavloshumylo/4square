package com.service;

import com.dao.SearchDao;
import com.dto.SearchResponseDto;
import com.dto.VenueDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchService {
    @Autowired
    private SearchDao search;

    @Autowired
    private VenueDto venue;

    @Autowired
    private SearchResponseDto searchResponse;

    public SearchResponseDto search(String city, String place) {
        try {
            searchResponse.getVenues().add(venue.jsonMapping(search.search(city, place)));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return searchResponse;
    }

}

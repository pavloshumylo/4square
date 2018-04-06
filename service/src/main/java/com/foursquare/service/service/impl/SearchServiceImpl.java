package com.foursquare.service.service.impl;

import com.foursquare.dao.SearchDao;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao search;

    public SearchResponseDto search(String city, String place) {
        SearchResponseDto searchResponse = new SearchResponseDto();
        try {
            searchResponse.getVenues().add(mapFromJson(search.search(city, place)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponse;
    }

    private VenueDto mapFromJson(String json) throws IOException {
        JsonNode venueDtoNode = new ObjectMapper().readTree(json);
        VenueDto venue = new VenueDto();
        venue.setId(venueDtoNode.get("response").get("venues").get(0).get("id").textValue());
        venue.setName(venueDtoNode.get("response").get("venues").get(0).get("name").textValue());
        venue.setAddress(venueDtoNode.get("response").get("venues").get(0).get("location").get("address").textValue());
        venue.setPhone(venueDtoNode.get("response").get("venues").get(0).get("contact").get("phone").textValue());
        return venue;
    }
}

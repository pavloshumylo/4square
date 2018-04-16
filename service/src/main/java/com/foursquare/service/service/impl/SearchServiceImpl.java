package com.foursquare.service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.foursquare.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao search;

    public SearchResponseDto search(String city, String place) {
        SearchResponseDto searchResponse = mapFromJson(search.search(city, place));
        return searchResponse;
    }

    private SearchResponseDto mapFromJson(String json) {
        SearchResponseDto searchResponse = new SearchResponseDto();

        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(json);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }

        JsonNode venuesNode = jsonNode.get("response").get("venues");
        venuesNode.forEach((venueNode) -> {
            VenueDto venueDto = new VenueDto();
            venueDto.setId(venueNode.get("id").textValue());
            venueDto.setName(venueNode.get("name").textValue());

            JsonNode phoneNode = venueNode.get("contact").get("phone");
            if (phoneNode != null) {
                venueDto.setPhone(phoneNode.textValue());
            } else {
                venueDto.setPhone("No phone available");
            }

            JsonNode addressNode = venueNode.get("location").get("address");
            if (venueNode.get("location").get("address") != null) {
                venueDto.setAddress(addressNode.textValue());
            } else {
                venueDto.setAddress("No address available");
            }
            searchResponse.getVenues().add(venueDto);
        });

        return searchResponse;
    }
}

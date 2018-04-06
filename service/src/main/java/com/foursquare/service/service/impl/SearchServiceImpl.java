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
import java.util.*;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private SearchDao search;

    public SearchResponseDto search(String city, String place) {
        SearchResponseDto searchResponse = new SearchResponseDto();
        try {
            mapFromJson(search.search(city, place), searchResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return searchResponse;
    }

    private void mapFromJson(String json, SearchResponseDto searchResponse) throws IOException {
        JsonNode venueDtoNode = new ObjectMapper().readTree(json);
        JsonNode jsonNode = venueDtoNode.get("response").get("venues");
        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            VenueDto venueDto = new VenueDto();
            JsonNode tempNode = elements.next();
            venueDto.setId(tempNode.get("id").textValue());
            venueDto.setName(tempNode.get("name").textValue());
            venueDto.setPhone(tempNode.get("contact").get("phone").textValue());
            venueDto.setAddress(tempNode.get("location").get("address").textValue());
            searchResponse.getVenues().add(venueDto);
        }
    }
}

package com.foursquare.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.dao.SearchDao;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.foursquare.service.SearchService;
import com.foursquare.validator.VenueValidatorFromDaoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao search;

    public SearchResponseDto search(String city, String place, String limit) {
        SearchResponseDto searchResponse = mapFromJson(search.search(city, place, limit));
        return searchResponse;
    }

    private SearchResponseDto mapFromJson(String json) {
        SearchResponseDto searchResponse = new SearchResponseDto();

        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(json);
        } catch (Exception e) {
            throw  new RuntimeException(e);
        }

        if (jsonNode.get("response") != null && jsonNode.get("response").get("venues") != null) {
            JsonNode venuesNode = jsonNode.get("response").get("venues");
            venuesNode.forEach((venueNode) -> {
                if (VenueValidatorFromDaoResponse.isValidVenue(venueNode)) {
                    VenueDto venueDto = new VenueDto();
                    venueDto.setId(venueNode.get("id").textValue());

                    if (venueNode.get("name") != null) {
                        venueDto.setName(venueNode.get("name").textValue());
                    }

                    if (venueNode.get("contact") != null && venueNode.get("contact").get("phone") != null) {
                        venueDto.setPhone(venueNode.get("contact").get("phone").textValue());
                    }

                    if (venueNode.get("location") != null && venueNode.get("location").get("address") != null) {
                        venueDto.setAddress(venueNode.get("location").get("address").textValue());
                    }

                    searchResponse.getVenues().add(venueDto);
                }
            });
        }

        return searchResponse;
    }
}

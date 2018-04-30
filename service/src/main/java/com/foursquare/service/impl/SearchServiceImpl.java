package com.foursquare.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.foursquare.dao.SearchDao;
import com.foursquare.dto.SearchResponseDto;
import com.foursquare.dto.VenueDto;
import com.foursquare.service.SearchService;
import com.foursquare.validator.DaoResponseVenueValidatior;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private SearchDao search;

    public SearchResponseDto search(String city, String place, String limit) {
        SearchResponseDto searchResponse = mapFromJson(search.search(city, place, limit));
        return searchResponse;
    }

    private SearchResponseDto mapFromJson(JsonNode jsonNode) {
        SearchResponseDto searchResponse = new SearchResponseDto();

        Optional<JsonNode> venuesNode = Optional.ofNullable(jsonNode).map(venueNode -> venueNode.get("response")).
                    map(responseNode -> responseNode.get("venues"));

        venuesNode.ifPresent(n -> n.forEach((venueNode) -> {
            if (DaoResponseVenueValidatior.isValidVenue(venueNode)) {
                VenueDto venueDto = new VenueDto();

                if(venueNode.get("id") != null) {
                    venueDto.setId(venueNode.get("id").textValue());
                }

                if(venueNode.get("name") != null) {
                    venueDto.setName(venueNode.get("name").textValue());
                }

                Optional.ofNullable(venueNode).map(venueOptionalNode -> venueOptionalNode.get("contact")).
                        map(contactNode -> contactNode.get("phone")).ifPresent(phoneNode ->
                        venueDto.setPhone(phoneNode.textValue()));

                Optional.ofNullable(venueNode).map(venueOptionalNode -> venueOptionalNode.get("location")).
                        map(locationNode -> locationNode.get("address")).ifPresent(addresssNode ->
                        venueDto.setAddress(addresssNode.textValue()));

                searchResponse.getVenues().add(venueDto);
            }
        }));

        return searchResponse;
    }
}

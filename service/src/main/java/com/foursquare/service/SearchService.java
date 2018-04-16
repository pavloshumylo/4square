package com.foursquare.service;

import com.foursquare.dto.SearchResponseDto;

public interface SearchService {

     SearchResponseDto search(String city, String query, String limit);
}

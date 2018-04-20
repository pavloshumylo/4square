package com.foursquare.controller;

import com.foursquare.dto.SearchResponseDto;
import com.foursquare.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(value = "/search")
    public SearchResponseDto search(@RequestParam(value = "near") String near, @RequestParam(value = "query") String query, @RequestParam(value = "limit") String limit) {
        return searchService.search(near, query, limit);
    }
}

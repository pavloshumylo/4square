package com.controller;

import com.dto.SearchResponseDto;
import com.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping(value = "/search")
    public SearchResponseDto search(@RequestParam(value = "near") String near, @RequestParam(value = "qwery") String qwery) {
        return searchService.search(near, qwery);
    }
}

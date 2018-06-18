package com.foursquare.endpoints;

import com.foursquare.dto.SearchResponseDto;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FoursquareClient {

    CompletableFuture<SearchResponseDto> searchVenuesByParams(String city, String query, String limit, String accessToken);
    CompletableFuture<Void> registerNewUser(User user);
    CompletableFuture<Void> saveVenueForUser(String fsId, String accessToken);
    CompletableFuture<Void> removeVenueForUser(String fsId, String accessToken);
    CompletableFuture<List<Venue>> getAllVenuesForUser(String accessToken);
}
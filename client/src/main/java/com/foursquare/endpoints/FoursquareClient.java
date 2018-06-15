package com.foursquare.endpoints;

import com.foursquare.dto.SearchResponseDto;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FoursquareClient {

    CompletableFuture<SearchResponseDto> invokeSearchEndPoint(String city, String query, String limit, String accessToken);
    CompletableFuture<Void> invokeRegistrationEndPoint(User user);
    CompletableFuture<Void> invokeSaveEndpoint(String fsId, String accessToken);
    CompletableFuture<Void> invokeDeleteEndpoint(String fsId, String accessToken);
    CompletableFuture<List<Venue>> invokeGetEndpoint(String accessToken);
}
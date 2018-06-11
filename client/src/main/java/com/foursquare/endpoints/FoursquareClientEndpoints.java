package com.foursquare.endpoints;

import com.foursquare.dto.SearchResponseDto;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface FoursquareClientEndpoints {

    CompletableFuture<ResponseEntity<SearchResponseDto>> invokeSearchEndPoint(String city, String query, String limit, String accessToken);
    CompletableFuture<ResponseEntity<ResponseEntity>> invokeRegistrationEndPoint(User user);
    CompletableFuture<ResponseEntity<ResponseEntity>> invokeSaveEndpoint(String fsId, String accessToken);
    CompletableFuture<ResponseEntity<ResponseEntity>> invokeDeleteEndpoint(String fsId, String accessToken);
    CompletableFuture<ResponseEntity<List<Venue>>> invokeGetEndpoint(String accessToken);
}
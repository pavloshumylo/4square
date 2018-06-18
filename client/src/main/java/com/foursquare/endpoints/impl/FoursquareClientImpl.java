package com.foursquare.endpoints.impl;

import com.foursquare.dto.SearchResponseDto;
import com.foursquare.endpoints.FoursquareClient;
import com.foursquare.entity.User;
import com.foursquare.entity.Venue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.web.client.AsyncRestTemplate;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class FoursquareClientImpl implements FoursquareClient {

    @Value("${foursquare.endPointHost:http://localhost:8080}")
    private String baseEndPointsUrl;

    private static final String urlSearchByParams = "{fourSquareEndPointHost}/search?near={city}&query={query}&limit={limit}&access_token={access_token}";
    private static final String urlRegisterationUser = "{fourSquareEndPointHost}/registration";
    private static final String urlSaveDeleteVenues = "{fourSquareEndPointHost}/venues/{fsId}?access_token={access_token}";
    private static final String urlGetVenues = "{fourSquareEndPointHost}/venues?access_token={access_token}";

    public CompletableFuture<SearchResponseDto> searchVenuesByParams(String city, String query, String limit, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ListenableFuture<ResponseEntity<SearchResponseDto>> listenableFuture = new AsyncRestTemplate().exchange(urlSearchByParams,
                HttpMethod.GET,
                httpEntity,
                SearchResponseDto.class,
                baseEndPointsUrl,
                city,
                query,
                limit,
                accessToken);

        return buildCompletableFuture(listenableFuture);
    }

    public CompletableFuture<Void> registerNewUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(user, headers);
        ListenableFuture<ResponseEntity<Void>> listenableFuture = new AsyncRestTemplate().exchange(urlRegisterationUser,
                HttpMethod.POST,
                httpEntity,
                Void.class,
                baseEndPointsUrl);

        return buildCompletableFuture(listenableFuture);
    }

    public CompletableFuture<Void> saveVenueForUser(String fsId, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ListenableFuture<ResponseEntity<Void>> listenableFuture = new AsyncRestTemplate().exchange(urlSaveDeleteVenues,
                HttpMethod.POST,
                httpEntity,
                Void.class,
                baseEndPointsUrl,
                fsId,
                accessToken);

        return buildCompletableFuture(listenableFuture);
    }

    public CompletableFuture<Void> removeVenueForUser(String fsId, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ListenableFuture<ResponseEntity<Void>> listenableFuture = new AsyncRestTemplate().exchange(urlSaveDeleteVenues,
                HttpMethod.DELETE,
                httpEntity,
                Void.class,
                baseEndPointsUrl,
                fsId,
                accessToken);

        return buildCompletableFuture(listenableFuture);
    }

    public CompletableFuture<List<Venue>> getAllVenuesForUser(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ListenableFuture<ResponseEntity<List<Venue>>> listenableFuture = new AsyncRestTemplate().exchange(urlGetVenues,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<Venue>>() {
                },
                baseEndPointsUrl,
                accessToken);

        return buildCompletableFuture(listenableFuture);
    }

    private static <T> CompletableFuture<T> buildCompletableFuture(ListenableFuture<ResponseEntity<T>> listenableFuture) {

        CompletableFuture<T> completable = new CompletableFuture<T>() {
            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                boolean result = listenableFuture.cancel(mayInterruptIfRunning);
                return result;
            }
        };

        listenableFuture.addCallback(new ListenableFutureCallback<ResponseEntity<T>>() {
            @Override
            public void onSuccess(ResponseEntity<T> result) {
                completable.complete(result.getBody());
            }

            @Override
            public void onFailure(Throwable t) {
                completable.completeExceptionally(t);
            }
        });

        return completable;
    }
}
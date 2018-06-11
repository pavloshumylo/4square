package com.foursquare.endpoints.impl;

import com.foursquare.dto.SearchResponseDto;
import com.foursquare.endpoints.FoursquareClientEndpoints;
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
public class FoursquareClientEndpointsImpl implements FoursquareClientEndpoints {

    @Value("${foursquare.host:http://localhost:8080/}")
    private String host;

    private static final String urlSearchByParams = "{fourSquareEndPointHost}search?near={city}&query={query}&limit={limit}&access_token={access_token}";
    private static final String urlRegisterationUser = "{fourSquareEndPointHost}registration";
    private static final String urlSaveDeleteVenues = "{fourSquareEndPointHost}venues/{fsId}?access_token={access_token}";
    private static final String urlGetVenues = "{fourSquareEndPointHost}venues?access_token={access_token}";

    public CompletableFuture<ResponseEntity<SearchResponseDto>> invokeSearchEndPoint(String city, String query, String limit, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ListenableFuture<ResponseEntity<SearchResponseDto>> listenableFuture = new AsyncRestTemplate().exchange(urlSearchByParams,
                HttpMethod.GET,
                httpEntity,
                SearchResponseDto.class,
                host,
                city,
                query,
                limit,
                accessToken);

        return buildCompletableFuture(listenableFuture);
    }

    public CompletableFuture<ResponseEntity<ResponseEntity>> invokeRegistrationEndPoint(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(user, headers);
        ListenableFuture<ResponseEntity<ResponseEntity>> listenableFuture = new AsyncRestTemplate().exchange(urlRegisterationUser,
                HttpMethod.POST,
                httpEntity,
                ResponseEntity.class,
                host);

        return buildCompletableFuture(listenableFuture);
    }

    public CompletableFuture<ResponseEntity<ResponseEntity>> invokeSaveEndpoint(String fsId, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ListenableFuture<ResponseEntity<ResponseEntity>> listenableFuture = new AsyncRestTemplate().exchange(urlSaveDeleteVenues,
                HttpMethod.POST,
                httpEntity,
                ResponseEntity.class,
                host,
                fsId,
                accessToken);

        return buildCompletableFuture(listenableFuture);
    }

    public CompletableFuture<ResponseEntity<ResponseEntity>> invokeDeleteEndpoint(String fsId, String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ListenableFuture<ResponseEntity<ResponseEntity>> listenableFuture = new AsyncRestTemplate().exchange(urlSaveDeleteVenues,
                HttpMethod.DELETE,
                httpEntity,
                ResponseEntity.class,
                host,
                fsId,
                accessToken);

        return buildCompletableFuture(listenableFuture);
    }

    public CompletableFuture<ResponseEntity<List<Venue>>> invokeGetEndpoint(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        ListenableFuture<ResponseEntity<List<Venue>>> listenableFuture = new AsyncRestTemplate().exchange(urlGetVenues,
                HttpMethod.GET,
                httpEntity,
                new ParameterizedTypeReference<List<Venue>>() {
                },
                host,
                accessToken);

        return buildCompletableFuture(listenableFuture);
    }

    private static <T> CompletableFuture<T> buildCompletableFuture(ListenableFuture<T> listenableFuture) {
        CompletableFuture<T> completable = new CompletableFuture<T>();

        listenableFuture.addCallback(new ListenableFutureCallback<T>() {
            @Override
            public void onSuccess(T result) { completable.complete(result); }

            @Override
            public void onFailure(Throwable t) {
                completable.completeExceptionally(t);
            }
        });

        return completable;
    }
}
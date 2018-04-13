package com.foursquare.dao.dao.impl;

import com.foursquare.dao.SearchDao;
import com.foursquare.dao.config.ApplicationConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class SearchDaoImpl implements SearchDao {

    @Autowired
    private ApplicationConfig applicationConfig;

    private static final String fourSquareQueryFirst = "near";
    private static final String fourSquareQuerySecond = "limit";
    private static final String fourSquareClientId = "client_id";
    private static final String fourSquareClientSecret = "client_secret";
    private static final String fourSquareVersion = "v";
    private static final String url = "{fourSquareApiHost}v2/venues/search?{fourSquareQueryFirst}={city}&{fourSquareQuerySecond}={limit}&{fourSquareClientId}={client_id}&{fourSquareClientSecret}={client_secret}&{fourSquareVersion}={version}";

    public String search(String city, String limit)  {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<?> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.exchange(url,
                HttpMethod.GET,
                httpEntity,
                String.class,
                applicationConfig.getFourSquareApiHost(),
                fourSquareQueryFirst,
                city,
                fourSquareQuerySecond,
                limit,
                fourSquareClientId,
                applicationConfig.getFourSquareApiClientId(),
                fourSquareClientSecret,
                applicationConfig.getFourSquareApiClientSecret(),
                fourSquareVersion,
                applicationConfig.getFourSquareApiVersion());
        return response.getBody().toString();
    }
}

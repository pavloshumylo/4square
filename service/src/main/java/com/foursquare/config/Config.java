package com.foursquare.config;

import com.foursquare.dao.SearchDao;
import com.foursquare.dao.dao.impl.SearchDaoImpl;
import com.foursquare.handler.ResponseErrorHandlerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class Config {

    @Bean
     public SearchDao searchDao(){
        return new SearchDaoImpl();
    }

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new ResponseErrorHandlerImpl());
        return restTemplate;
    }
}

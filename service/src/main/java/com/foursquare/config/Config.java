package com.foursquare.config;

import com.foursquare.dao.SearchDao;
import com.foursquare.dao.impl.SearchDaoImpl;
import com.foursquare.handler.ResponseErrorHandlerImpl;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import java.util.TimeZone;

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

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonObjectMapperCustomization() {
        return jacksonObjectMapperBuilder ->
                jacksonObjectMapperBuilder.timeZone(TimeZone.getDefault());
    }
}

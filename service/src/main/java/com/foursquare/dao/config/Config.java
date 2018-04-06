package com.foursquare.dao.config;

import com.foursquare.dao.SearchDao;
import com.foursquare.dao.dao.impl.MockSearchDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public SearchDao mockBean(){
        return new MockSearchDaoImpl();
    }
}

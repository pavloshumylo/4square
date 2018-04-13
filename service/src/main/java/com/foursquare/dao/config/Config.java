package com.foursquare.dao.config;

import com.foursquare.dao.SearchDao;
import com.foursquare.dao.dao.impl.SearchDaoImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
     public SearchDao searchDao(){
        return new SearchDaoImpl();
    }
}

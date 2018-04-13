package com.foursquare.dao.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties
public class ApplicationConfig {

    private String fourSquareApiHost;
    private String fourSquareApiClientId;
    private String fourSquareApiClientSecret;
    private String fourSquareApiVersion;

    public String getFourSquareApiHost() {
        return fourSquareApiHost;
    }

    public void setFourSquareApiHost(String fourSquareApiHost) {
        this.fourSquareApiHost = fourSquareApiHost;
    }

    public String getFourSquareApiClientId() {
        return fourSquareApiClientId;
    }

    public void setFourSquareApiClientId(String fourSquareApiClientId) {
        this.fourSquareApiClientId = fourSquareApiClientId;
    }

    public String getFourSquareApiClientSecret() {
        return fourSquareApiClientSecret;
    }

    public void setFourSquareApiClientSecret(String fourSquareApiClientSecret) {
        this.fourSquareApiClientSecret = fourSquareApiClientSecret;
    }

    public String getFourSquareApiVersion() {
        return fourSquareApiVersion;
    }

    public void setFourSquareApiVersion(String fourSquareApiVersion) {
        this.fourSquareApiVersion = fourSquareApiVersion;
    }
}
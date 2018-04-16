package com.foursquare.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "foursquare")
public class FourSquareProperties {

    private String apiHost;
    private String apiClientId;
    private String apiClientSecret;
    private String apiVersion;

    public String getApiHost() {
        return apiHost;
    }

    public void setApiHost(String apiHost) {
        this.apiHost = apiHost;
    }

    public String getApiClientId() {
        return apiClientId;
    }

    public void setApiClientId(String apiClientId) {
        this.apiClientId = apiClientId;
    }

    public String getApiClientSecret() {
        return apiClientSecret;
    }

    public void setApiClientSecret(String apiClientSecret) {
        this.apiClientSecret = apiClientSecret;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }
}
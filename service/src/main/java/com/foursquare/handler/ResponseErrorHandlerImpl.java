package com.foursquare.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.exception.FourSquareApiException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.io.InputStream;

public class ResponseErrorHandlerImpl extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) {
        JsonNode jsonNode = null;
        try {
            InputStream inputStream = response.getBody();
            jsonNode = new ObjectMapper().readTree(inputStream);
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }

        if(jsonNode.get("meta").get("code").intValue() != 200) {
            FourSquareApiException apiException = new FourSquareApiException();
            apiException.setCode(jsonNode.get("meta").get("code").intValue());
            apiException.setMessage(jsonNode.get("meta").get("errorDetail").textValue());
            throw apiException;
        }
    }
}

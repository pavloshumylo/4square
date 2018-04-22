package com.foursquare.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.exception.FourSquareApiException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class ResponseErrorHandlerImpl extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) {
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(response.getBody());
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }

        FourSquareApiException apiException = new FourSquareApiException(
                jsonNode.get("meta").get("code").intValue(),
                jsonNode.get("meta").get("errorDetail").textValue());
        throw apiException;
    }
}

package com.foursquare.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.exception.FourSquareApiException;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.Optional;

public class ResponseErrorHandlerImpl extends DefaultResponseErrorHandler {

    @Override
    public void handleError(ClientHttpResponse response) {
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(response.getBody());
        } catch (IOException e) {
            throw  new RuntimeException(e);
        }

        int code = Optional.ofNullable(jsonNode.get("meta")).map(metaNode -> metaNode.get("code"))
                .map(codeNode -> codeNode.intValue()).orElse(500);
        String message = Optional.ofNullable(jsonNode.get("meta")).map(metaNode -> metaNode.get("errorDetail"))
                .map(errorDetailNode -> errorDetailNode.textValue()).orElse("Unknown Error");

        throw new FourSquareApiException(code, message);
    }
}

package com.foursquare.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.exception.FourSquareApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.Optional;

public class ResponseErrorHandlerImpl extends DefaultResponseErrorHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResponseErrorHandlerImpl.class);

    @Override
    public void handleError(ClientHttpResponse response) {
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(response.getBody());
        } catch (IOException e) {
            LOG.error("Exception thrown: " + e + ", message: " + e.getMessage());
            throw  new RuntimeException(e);
        }

        int code = Optional.ofNullable(jsonNode.get("meta")).map(metaNode -> metaNode.get("code"))
                .map(codeNode -> codeNode.intValue()).orElse(500);
        String message = Optional.ofNullable(jsonNode.get("meta")).map(metaNode -> metaNode.get("errorDetail"))
                .map(errorDetailNode -> errorDetailNode.textValue()).orElse("Unknown Error");

        LOG.error("FourSquareApiException thrown with code: " + code + " , message: " + message);
        throw new FourSquareApiException(code, message);
    }
}

package com.foursquare.handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foursquare.exception.FourSquareApiException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.Optional;

public class ResponseErrorHandlerImpl extends DefaultResponseErrorHandler {

    private static final Log log = LogFactory.getLog(ResponseErrorHandlerImpl.class);

    @Override
    public void handleError(ClientHttpResponse response) {
        JsonNode jsonNode = null;
        try {
            jsonNode = new ObjectMapper().readTree(response.getBody());
        } catch (IOException e) {
            log.error("Exception thrown: " + e + ", message: " + e.getMessage());
            throw  new RuntimeException(e);
        }

        int code = Optional.ofNullable(jsonNode.get("meta")).map(metaNode -> metaNode.get("code"))
                .map(codeNode -> codeNode.intValue()).orElse(500);
        String message = Optional.ofNullable(jsonNode.get("meta")).map(metaNode -> metaNode.get("errorDetail"))
                .map(errorDetailNode -> errorDetailNode.textValue()).orElse("Unknown Error");

        log.error("FourSquareApiException thrown with code: " + code + " , message: " + message);
        throw new FourSquareApiException(code, message);
    }
}

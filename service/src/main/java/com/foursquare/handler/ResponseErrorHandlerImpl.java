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

        boolean codeNodeIsNull = true;
        boolean errorDetailNodeIsNull = true;
        if (jsonNode.get("meta") != null) {
            if (jsonNode.get("meta").get("code") != null) {
                codeNodeIsNull = false;
            }
            if (jsonNode.get("meta").get("errorDetail") != null) {
                errorDetailNodeIsNull = false;
            }
        }

        FourSquareApiException apiException = null;
        if (codeNodeIsNull && errorDetailNodeIsNull) {
            apiException = new FourSquareApiException();
        } else if (codeNodeIsNull) {
            apiException = new FourSquareApiException(jsonNode.get("meta").get("errorDetail").textValue());
        } else if (errorDetailNodeIsNull) {
            apiException = new FourSquareApiException(jsonNode.get("meta").get("code").intValue());
        } else {
            apiException = new FourSquareApiException(jsonNode.get("meta").get("code").intValue(),
                    jsonNode.get("meta").get("errorDetail").textValue());
        }
        throw apiException;
    }
}

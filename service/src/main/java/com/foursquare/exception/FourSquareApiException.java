package com.foursquare.exception;

public class FourSquareApiException extends RuntimeException {

    private int code;

    public FourSquareApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

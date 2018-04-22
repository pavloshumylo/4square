package com.foursquare.exception;

public class FourSquareApiException extends RuntimeException {

    private int code;

    public FourSquareApiException() {
        super();
    }

    public FourSquareApiException(int code, String message) {
        super(message);
        this.code = code;
    }

    public FourSquareApiException(int code) {
        super();
        this.code = code;
    }

    public FourSquareApiException(String message) {
        super(message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}

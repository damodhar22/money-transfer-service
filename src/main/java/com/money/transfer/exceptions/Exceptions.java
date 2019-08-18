package com.money.transfer.exceptions;

public class Exceptions extends RuntimeException {
    private final int statusCode;

    public Exceptions(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

}

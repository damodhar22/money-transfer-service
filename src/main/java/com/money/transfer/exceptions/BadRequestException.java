package com.money.transfer.exceptions;

import io.undertow.util.StatusCodes;

public class BadRequestException extends Exceptions {
    public BadRequestException(String message) {
        super(StatusCodes.BAD_REQUEST, message);
    }
}


package com.money.transfer.exceptions;

import io.undertow.util.StatusCodes;

public class NotFoundException extends Exceptions {
    public NotFoundException(String message) {
        super(StatusCodes.NOT_FOUND, message);
    }
}


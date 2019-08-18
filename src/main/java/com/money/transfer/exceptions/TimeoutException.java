package com.money.transfer.exceptions;

import io.undertow.util.StatusCodes;

public class TimeoutException extends Exceptions {
    public TimeoutException() {
        super(StatusCodes.REQUEST_TIME_OUT, "Operation timed out");
    }
}

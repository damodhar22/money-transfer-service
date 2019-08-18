package com.money.transfer.exceptions;

import com.money.transfer.exchange.Exchange;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.util.StatusCodes;

public class ErrorHandlers {
    public static void handleNotFoundException(HttpServerExchange exchange) {
        NotFoundException ex = (NotFoundException) exchange.getAttachment(ExceptionHandler.THROWABLE);
        exchange.setStatusCode(ex.getStatusCode());
        Exchange.body().sendPlainText(exchange, ex.getMessage());
    }

    public static void handleBadRequestException(HttpServerExchange exchange) {
        BadRequestException ex = (BadRequestException) exchange.getAttachment(ExceptionHandler.THROWABLE);
        exchange.setStatusCode(ex.getStatusCode());
        Exchange.body().sendPlainText(exchange, ex.getMessage());
    }

    public static void handleTimeoutException(HttpServerExchange exchange) {
        TimeoutException ex = (TimeoutException) exchange.getAttachment(ExceptionHandler.THROWABLE);
        exchange.setStatusCode(ex.getStatusCode());
        Exchange.body().sendPlainText(exchange, ex.getMessage());
    }

    public static void handleAllExceptions(HttpServerExchange exchange){
            exchange.setStatusCode(StatusCodes.INTERNAL_SERVER_ERROR);
            Exchange.body().sendPlainText(exchange, "Internal Server Error!");
    }
}


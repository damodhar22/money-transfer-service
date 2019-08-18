package com.money.transfer.exchange;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public interface PlainTextSender {

    default void sendPlainText(HttpServerExchange exchange, String data) {
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send(data);
    }
}

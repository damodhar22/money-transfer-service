package com.money.transfer.exchange;

import io.undertow.server.HttpServerExchange;

import java.util.Deque;
import java.util.Optional;

public interface PathParams {

    default Optional<String> pathParam(HttpServerExchange exchange, String name) {
        return Optional.ofNullable(exchange.getQueryParameters().get(name))
                       .map(Deque::getFirst);
    }

    default Optional<Long> pathParamAsLong(HttpServerExchange exchange, String name) {
        return pathParam(exchange, name).map(Long::parseLong);
    }
}

package com.money.transfer.routing;

import io.undertow.server.HttpServerExchange;
import io.undertow.util.Headers;

public class FallBackService {

    public static void pageNotFoundHandler(HttpServerExchange exchange) {
        exchange.setStatusCode(404);
        exchange.getResponseHeaders().put(Headers.CONTENT_TYPE, "text/plain");
        exchange.getResponseSender().send("Page Not Found!!");
    }
}

package com.money.transfer.routing;

import com.money.transfer.exchange.Json;
import io.undertow.Undertow;
import io.undertow.UndertowOptions;
import io.undertow.server.handlers.BlockingHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Server {
    private static final Logger logger = LogManager.getLogger(Json.class);
    private static final int PORT = 9090;
    private static final String HOST = "127.0.0.1";
    private static Undertow undertow;

    public static void start() {
        undertow = Undertow.builder()
                .setServerOption(UndertowOptions.ENABLE_HTTP2, true)
                .addHttpListener(PORT, HOST, new BlockingHandler(Routes.ROOT))
                .build();
        undertow.start();
        logger.info("Started server at port:{}",PORT);
    }

    public static void stop() {
        undertow.stop();
        logger.info("Stopped server at port:{}",PORT);
    }
}

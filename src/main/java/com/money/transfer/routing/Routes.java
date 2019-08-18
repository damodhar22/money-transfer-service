package com.money.transfer.routing;

import com.money.transfer.exceptions.BadRequestException;
import com.money.transfer.exceptions.ErrorHandlers;
import com.money.transfer.exceptions.NotFoundException;
import com.money.transfer.exceptions.TimeoutException;
import com.money.transfer.service.AccountService;
import com.money.transfer.service.TransactionService;
import io.undertow.Handlers;
import io.undertow.server.HttpHandler;
import io.undertow.server.RoutingHandler;

public class Routes {
    public static final RoutingHandler ROUTES = new RoutingHandler()
            .get("/accounts", AccountService::listAccounts)
            .get("/accounts/{accountId}", AccountService::getAccount)
            .post("/accounts", AccountService::createAccount)
            .delete("/accounts/{accountId}", AccountService::deleteAccount)
            .post("/accounts/deposit", TransactionService::depositInAccount)
            .post("/accounts/withdraw", TransactionService::withdrawFromAccount)
            .post("/transfer", TransactionService::transfer)
            .setFallbackHandler(FallBackService::pageNotFoundHandler);

    public static final HttpHandler ROOT = Handlers.exceptionHandler(ROUTES)
            .addExceptionHandler(BadRequestException.class, ErrorHandlers::handleBadRequestException)
            .addExceptionHandler(NotFoundException.class, ErrorHandlers::handleNotFoundException)
            .addExceptionHandler(TimeoutException.class, ErrorHandlers::handleTimeoutException)
            .addExceptionHandler(Throwable.class, ErrorHandlers::handleAllExceptions);
}

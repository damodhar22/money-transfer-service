package com.money.transfer.service;

import com.money.transfer.dao.AccountDao;
import com.money.transfer.exceptions.ApiException;
import com.money.transfer.exceptions.NotFoundException;
import com.money.transfer.exchange.AccountRequests;
import com.money.transfer.exchange.Exchange;
import com.money.transfer.model.NewAccountRequestModel;
import com.money.transfer.utility.Validations;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

import java.util.List;

public class AccountService {
    private static final AccountRequests ACCOUNT_REQUESTS = new AccountRequests();
    private static final AccountDao ACCOUNT_DAO = AccountDao.getInstance();

    public static void createAccount(HttpServerExchange exchange) {
        NewAccountRequestModel newAccountRequest = ACCOUNT_REQUESTS.accountRequest(exchange);
        Validations.validateBalance(newAccountRequest.getBalance());
        Account account = ACCOUNT_DAO.createAccount(newAccountRequest.getAccountHolderName(), newAccountRequest.getBalance());
        if (null == account) {
            throw new ApiException(String.format("Error creating account for user %s.", newAccountRequest.getAccountHolderName()));
        }
        exchange.setStatusCode(StatusCodes.CREATED);
        Exchange.body().sendJson(exchange, account);
    }

    public static void getAccount(HttpServerExchange exchange) {
        Long accountId = ACCOUNT_REQUESTS.accountId(exchange);
        Account account = ACCOUNT_DAO.getAccount(accountId);
        if (null == account) {
            throw new NotFoundException(String.format("Account %s not found.", accountId));
        }
        exchange.setStatusCode(StatusCodes.OK);
        Exchange.body().sendJson(exchange, account);
    }

    public static void deleteAccount(HttpServerExchange exchange) {
        Long accountId = ACCOUNT_REQUESTS.accountId(exchange);
        if (!ACCOUNT_DAO.deleteAccount(accountId)) {
            throw new NotFoundException(String.format("Account %s not found.", accountId));
        }
        exchange.setStatusCode(StatusCodes.NO_CONTENT);
        exchange.endExchange();
    }

    public static void listAccounts(HttpServerExchange exchange) {
        List<Account> accounts = ACCOUNT_DAO.listAccounts();
        exchange.setStatusCode(StatusCodes.OK);
        Exchange.body().sendJson(exchange, accounts);
    }
}



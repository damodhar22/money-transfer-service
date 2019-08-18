package com.money.transfer.service;

import com.money.transfer.dao.AccountDao;
import com.money.transfer.exceptions.NotFoundException;
import com.money.transfer.exceptions.TimeoutException;
import com.money.transfer.exchange.AccountRequests;
import com.money.transfer.exchange.Exchange;
import com.money.transfer.model.CreditDebitRequestModel;
import com.money.transfer.model.TransferRequest;
import com.money.transfer.utility.Validations;
import io.undertow.server.HttpServerExchange;
import io.undertow.util.StatusCodes;

import java.math.BigDecimal;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

public class TransactionService {

    private static final AccountDao ACCOUNT_DAO = AccountDao.getInstance();
    private static final AccountRequests ACCOUNT_REQUESTS = new AccountRequests();
    private static final int TIMEOUT_PERIOD = 60;

    public static void depositInAccount(HttpServerExchange exchange) {
        CreditDebitRequestModel creditDebitRequestModel = ACCOUNT_REQUESTS.creditDebit(exchange);
        Account account = ACCOUNT_DAO.getAccount(creditDebitRequestModel.getAccountId());
        if (null == account) {
            throw new NotFoundException(String.format("Account %s not found.", creditDebitRequestModel.getAccountId()));
        }

        if (acquireLock(account.lock)) {
            try {
                account.credit(creditDebitRequestModel.getAmount());
            } finally {
                account.lock.unlock();
            }
        }

        exchange.setStatusCode(StatusCodes.OK);
        Exchange.body().sendJson(exchange, account);
    }

    public static void withdrawFromAccount(HttpServerExchange exchange) {
        CreditDebitRequestModel creditDebitRequestModel = ACCOUNT_REQUESTS.creditDebit(exchange);
        Account account = ACCOUNT_DAO.getAccount(creditDebitRequestModel.getAccountId());
        if (null == account) {
            throw new NotFoundException(String.format("Account %s not found.", creditDebitRequestModel.getAccountId()));
        }

        if (acquireLock(account.lock)) {
            try {
                account.debit(creditDebitRequestModel.getAmount());
            } finally {
                account.lock.unlock();
            }
        }

        exchange.setStatusCode(StatusCodes.OK);
        Exchange.body().sendJson(exchange, account);
    }

    public static void transfer(HttpServerExchange exchange) {
        TransferRequest transferRequest = ACCOUNT_REQUESTS.transfer(exchange);
        Account payerAccount = ACCOUNT_DAO.getAccount(transferRequest.getPayerAccountId());
        Account payeeAccount = ACCOUNT_DAO.getAccount(transferRequest.getPayeeAccountId());
        if (null == payerAccount || null == payeeAccount) {
            throw new NotFoundException(String.format("Account %s or %s not found", transferRequest.getPayerAccountId(), transferRequest.getPayeeAccountId()));
        }

        // this is done to improve performance by avoiding timeouts while acquiring lock
        Lock firstLock;
        Lock secondLock;
        if (payerAccount.getAccountId() > payeeAccount.getAccountId()) {
            firstLock = payerAccount.lock;
            secondLock = payeeAccount.lock;
        } else {
            firstLock = payeeAccount.lock;
            secondLock = payerAccount.lock;
        }

        if (acquireLock(firstLock)) {
            try {
                if (acquireLock(secondLock)) {
                    try {
                        executeTransfer(transferRequest.getTransferAmount(), payerAccount, payeeAccount);
                    } finally {
                        secondLock.unlock();
                    }
                }
            } finally {
                firstLock.unlock();
            }
        }

        exchange.setStatusCode(StatusCodes.OK);
        exchange.endExchange();
    }

    private static void executeTransfer(BigDecimal transferAmount, Account payerAccount, Account payeeAccount) {
        Validations.validateDebit(payerAccount.getBalance(), transferAmount);
        payerAccount.debit(transferAmount);
        payeeAccount.credit(transferAmount);
    }

    private static boolean acquireLock(Lock lock) {
        try {
            if (lock.tryLock(TIMEOUT_PERIOD, TimeUnit.SECONDS)) {
                return true;
            }
            throw new TimeoutException();
        } catch (InterruptedException e) {
            throw new TimeoutException();
        }
    }
}

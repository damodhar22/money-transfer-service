package com.money.transfer.dao;

import com.money.transfer.service.Account;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class AccountDao {
    private static final AccountDao accountDao = new AccountDao();
    private final ConcurrentMap<Long, Account> accountMap;

    public AccountDao() {
        this.accountMap = new ConcurrentHashMap<>();
    }

    public static AccountDao getInstance() {
        return accountDao;
    }

    public Account createAccount(String accountHolderName, BigDecimal balance) {
        Account account = new Account(accountHolderName, balance);
        if (null != accountMap.putIfAbsent(account.getAccountId(), account)) {
            return null;
        }
        return account;
    }

    public Account getAccount(long accountId) {
        return accountMap.get(accountId);
    }

    public boolean deleteAccount(long accountId) {
        return null != accountMap.remove(accountId);
    }

    public List<Account> listAccounts() {
        return accountMap.values()
                .stream()
                .sorted(Comparator.comparing(Account::getAccountId))
                .collect(Collectors.toList());
    }

    public void deleteAllAccounts() {
        synchronized (accountMap) {
            accountMap.clear();
        }
    }
}

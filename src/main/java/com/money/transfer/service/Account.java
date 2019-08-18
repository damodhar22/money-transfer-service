package com.money.transfer.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.money.transfer.utility.Validations;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Account {

    private static final AtomicLong accountIdGenerator = new AtomicLong(1);
    @JsonIgnore
    public final Lock lock = new ReentrantLock();
    private long accountId;
    private String accountHolderName;
    // declared volatile to avoid read thread
    private volatile BigDecimal balance;

    public Account(String accountHolderName, BigDecimal balance) {
        Validations.validateBalance(balance);
        this.accountHolderName = accountHolderName;
        this.balance = balance;
        this.accountId = accountIdGenerator.getAndIncrement();
    }

    public long getAccountId() {
        return accountId;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BigDecimal credit(BigDecimal amount) {
        Validations.validateBalance(amount);
        balance = balance.add(amount);
        return balance;
    }

    public BigDecimal debit(BigDecimal amount) {
        Validations.validateBalance(amount);
        Validations.validateDebit(balance, amount);
        balance = balance.subtract(amount);
        return balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId == account.accountId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId);
    }
}

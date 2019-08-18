package com.money.transfer.dao;

import com.money.transfer.service.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AccountDaoTest {

    private AccountDao accountDao = AccountDao.getInstance();

    @BeforeEach
    void reset() {
        accountDao.deleteAllAccounts();
    }

    @Test
    @DisplayName("Should be able to create new account")
    void createAccount() {
        accountDao.createAccount("James Bond", new BigDecimal("1000"));
        assertEquals(1, accountDao.listAccounts().size());
        Account account = accountDao.listAccounts().get(0);
        assertNotNull(account);
        assertEquals("James Bond", account.getAccountHolderName());
        assertEquals(new BigDecimal("1000"), account.getBalance());
    }

    @Test
    @DisplayName("Should be able retrieve existing account by id")
    void getAccount() {
        accountDao.createAccount("James Bond", new BigDecimal("1000"));
        assertEquals(1, accountDao.listAccounts().size());
        Account account = accountDao.listAccounts().get(0);
        assertNotNull(account);
        long id = account.getAccountId();
        assertEquals(account, accountDao.getAccount(id));
    }

    @Test
    @DisplayName("Should be able to delete an account by id")
    void deleteAccount() {
        accountDao.createAccount("James Bond", new BigDecimal("1000"));
        assertEquals(1, accountDao.listAccounts().size());
        Account account = accountDao.listAccounts().get(0);
        assertNotNull(account);
        accountDao.deleteAccount(account.getAccountId());
        assertEquals(0, accountDao.listAccounts().size());
    }

    @Test
    @DisplayName("Should be able to list all the accounts")
    void listAccounts() {
        accountDao.createAccount("James Bond", new BigDecimal("1000"));
        accountDao.createAccount("John Doe", new BigDecimal("2000"));
        assertEquals(2, accountDao.listAccounts().size());
    }

    @Test
    @DisplayName("Should be able to delete all the accounts")
    void deleteAllAccounts(){
        accountDao.createAccount("James Bond", new BigDecimal("1000"));
        accountDao.createAccount("John Doe", new BigDecimal("2000"));
        assertEquals(2, accountDao.listAccounts().size());
        accountDao.deleteAllAccounts();
        assertEquals(0, accountDao.listAccounts().size());
    }
}

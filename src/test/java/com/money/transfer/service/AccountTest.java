package com.money.transfer.service;

import com.money.transfer.exceptions.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    @Test
    @DisplayName("Should able to credit in account")
    void credit() {
        Account account= new Account("James Bond", new BigDecimal("100"));
        account.credit(new BigDecimal("100"));
        assertEquals(new BigDecimal(200), account.getBalance());
    }

    @Test
    @DisplayName("Should throw exception in case invalid credit value")
    void creditFailTest() {
        Account account= new Account("James Bond", new BigDecimal("100"));
        assertThrows(BadRequestException.class, ()->account.credit(new BigDecimal("-100")));
    }

    @Test
    @DisplayName("Should able to debit from account")
    void debit() {
        Account account= new Account("James Bond", new BigDecimal("200"));
        account.debit(new BigDecimal("100"));
        assertEquals(new BigDecimal(100), account.getBalance());
    }

    @Test
    @DisplayName("Should throw exception in case invalid debit value")
    void debitFailTest() {
        Account account= new Account("James Bond", new BigDecimal("200"));
        assertThrows(BadRequestException.class, ()->account.debit(new BigDecimal("-100")));
    }
}
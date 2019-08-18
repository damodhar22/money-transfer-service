package com.money.transfer.utility;

import com.money.transfer.exceptions.BadRequestException;
import com.money.transfer.exceptions.NotFoundException;
import io.undertow.util.StatusCodes;

import java.math.BigDecimal;
import java.util.Objects;

public class Validations {

    public static void validateBalance(BigDecimal balance) {
         if(Objects.isNull(balance) || BigDecimal.ZERO.compareTo(balance) > 0){
             throw new BadRequestException("Invalid amount/balance value");
         }
    }

    public static void validateDebit(BigDecimal balance,BigDecimal debitAmount) {
        if(Objects.isNull(balance) || balance.compareTo(debitAmount) < 0){
            throw new BadRequestException("Insufficient funds available in account");
        }
    }
}

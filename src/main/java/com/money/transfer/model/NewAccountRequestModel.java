package com.money.transfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import java.math.BigDecimal;

public class NewAccountRequestModel {

    private static final TypeReference<NewAccountRequestModel> typeRef = new TypeReference<NewAccountRequestModel>() {
    };
    @JsonProperty(required = true)
    private String accountHolderName;
    @JsonProperty(required = true)
    private BigDecimal balance;

    public static TypeReference<NewAccountRequestModel> typeRef() {
        return typeRef;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}

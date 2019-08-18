package com.money.transfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import java.math.BigDecimal;

public class CreditDebitRequestModel {
    @JsonProperty(required = true)
    long accountId;

    @JsonProperty(required = true)
    BigDecimal amount;

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    private static final TypeReference<CreditDebitRequestModel> typeRef = new TypeReference<CreditDebitRequestModel>() {};
    public static TypeReference<CreditDebitRequestModel> typeRef() {
        return typeRef;
    }

}

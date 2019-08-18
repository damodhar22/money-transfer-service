package com.money.transfer.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;

import java.math.BigDecimal;

public class TransferRequest {

    @JsonProperty(required = true)
    private long payerAccountId;

    @JsonProperty(required = true)
    private long payeeAccountId;

    @JsonProperty(required = true)
    private BigDecimal transferAmount;

    public long getPayerAccountId() {
        return payerAccountId;
    }

    public void setPayerAccountId(long payerAccountId) {
        this.payerAccountId = payerAccountId;
    }

    public long getPayeeAccountId() {
        return payeeAccountId;
    }

    public void setPayeeAccountId(long payeeAccountId) {
        this.payeeAccountId = payeeAccountId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    private static final TypeReference<TransferRequest> typeRef = new TypeReference<TransferRequest>() {};
    public static TypeReference<TransferRequest> typeRef() {
        return typeRef;
    }
}

package com.money.transfer.exchange;

import com.money.transfer.model.CreditDebitRequestModel;
import com.money.transfer.model.NewAccountRequestModel;
import com.money.transfer.model.TransferRequest;
import io.undertow.server.HttpServerExchange;

public class AccountRequests {

    public Long accountId(HttpServerExchange exchange) {
        return Exchange.pathParams().pathParamAsLong(exchange, "accountId").orElse(null);
    }

    public NewAccountRequestModel accountRequest(HttpServerExchange exchange) {
        return Exchange.body().parseJson(exchange, NewAccountRequestModel.typeRef());
    }

    public CreditDebitRequestModel creditDebit(HttpServerExchange exchange) {
        return Exchange.body().parseJson(exchange, CreditDebitRequestModel.typeRef());
    }

    public TransferRequest transfer(HttpServerExchange exchange) {
        return Exchange.body().parseJson(exchange, TransferRequest.typeRef());
    }
}


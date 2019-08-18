package com.money.transfer.exchange;

public class Exchange {
    private static final BodyImpl BODY = new BodyImpl() {
    };

    private static final PathParamImpl PATHPARAMS = new PathParamImpl() {
    };

    public static BodyImpl body() {
        return BODY;
    }

    public static PathParamImpl pathParams() {
        return PATHPARAMS;
    }

    public interface BodyImpl extends
            JsonSender,
            JsonParser,
            PlainTextSender {
    }

    public interface PathParamImpl extends PathParams {
    }
}


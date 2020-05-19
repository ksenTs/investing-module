package com.investing.model;

public enum ExchangeToolType {
    SHARE("shares"),
    INDEX("index");

    private String code;

    ExchangeToolType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}

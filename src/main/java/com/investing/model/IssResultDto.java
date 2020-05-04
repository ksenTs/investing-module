package com.investing.model;

public class IssResultDto<T> {
    private T marketdata;

    public T getMarketdata() {
        return marketdata;
    }

    public void setMarketdata(T marketdata) {
        this.marketdata = marketdata;
    }
}

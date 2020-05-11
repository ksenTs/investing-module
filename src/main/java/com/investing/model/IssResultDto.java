package com.investing.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IssResultDto<T> {
    private T marketdata;
    private T securities;
    private T content;
    private T sitenews;

    public T getMarketdata() {
        return marketdata;
    }

    public void setMarketdata(T marketdata) {
        this.marketdata = marketdata;
    }

    public T getSecurities() {
        return securities;
    }

    public void setSecurities(T securities) {
        this.securities = securities;
    }

    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public T getSitenews() {
        return sitenews;
    }

    public void setSitenews(T sitenews) {
        this.sitenews = sitenews;
    }
}

package com.investing.model;

public class ExchangeTool {
    private String code;
    private String shortName;
    private String name;
    private Double value;
    private Double delta;

    public ExchangeTool() {}

    public ExchangeTool(String code, String shortName, String name) {
        this.code = code;
        this.shortName = shortName;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }
}

package com.investing.model;

import org.springframework.util.StringUtils;

public class IndexDto {
    private String code;
    private String shortName;
    private String name;

    private Double value;
    private Double delta;
    private Double openValue;
    private Double monthlyDelta;
    private Double yearlyDelta;
    private Double lowestValue;
    private Double highestValue;
    private Double capitalization;

    public IndexDto() {}

    public IndexDto(String code, String shortName, String name) {
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

    public Double getOpenValue() {
        return openValue;
    }

    public void setOpenValue(Double openValue) {
        this.openValue = openValue;
    }

    public Double getMonthlyDelta() {
        return monthlyDelta;
    }

    public void setMonthlyDelta(Double monthlyDelta) {
        this.monthlyDelta = monthlyDelta;
    }

    public Double getYearlyDelta() {
        return yearlyDelta;
    }

    public void setYearlyDelta(Double yearlyDelta) {
        this.yearlyDelta = yearlyDelta;
    }

    public Double getLowestValue() {
        return lowestValue;
    }

    public void setLowestValue(Double lowestValue) {
        this.lowestValue = lowestValue;
    }

    public Double getHighestValue() {
        return highestValue;
    }

    public void setHighestValue(Double highestValue) {
        this.highestValue = highestValue;
    }

    public Double getCapitalization() {
        return capitalization;
    }

    public void setCapitalization(Double capitalization) {
        this.capitalization = capitalization;
    }
}

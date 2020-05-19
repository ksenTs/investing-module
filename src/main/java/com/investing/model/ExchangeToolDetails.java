package com.investing.model;

public class ExchangeToolDetails extends ExchangeTool {
    private Double openValue;
    private Double monthlyDelta;
    private Double yearlyDelta;
    private Double lowestValue;
    private Double highestValue;
    private Double capitalization;

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

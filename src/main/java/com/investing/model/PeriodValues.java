package com.investing.model;

import java.util.ArrayList;
import java.util.List;

public class PeriodValues {
    private List<Double> high = new ArrayList<>();
    private List<Double> low = new ArrayList<>();
    private List<String> dates = new ArrayList<>();


    public List<Double> getHigh() {
        return high;
    }

    public void setHigh(List<Double> high) {
        this.high = high;
    }

    public List<Double> getLow() {
        return low;
    }

    public void setLow(List<Double> low) {
        this.low = low;
    }

    public List<String> getDates() {
        return dates;
    }

    public void setDates(List<String> dates) {
        this.dates = dates;
    }
}

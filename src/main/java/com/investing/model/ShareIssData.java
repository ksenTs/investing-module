package com.investing.model;

import java.util.ArrayList;
import java.util.List;

public class ShareIssData {
    private List<String> columns = new ArrayList<>();
    private List<List<String>> data = new ArrayList<>();

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public List<List<String>> getData() {
        return data;
    }

    public void setData(List<List<String>> data) {
        this.data = data;
    }
}

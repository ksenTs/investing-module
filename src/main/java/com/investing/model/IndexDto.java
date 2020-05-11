package com.investing.model;

import org.springframework.util.StringUtils;

public class IndexDto {
    private String indexId;
    private String shortName;
    private String name;
    private Double currentValue;

    public IndexDto(String indexId, String shortName, String name) {
        this.indexId = indexId;
        this.shortName = shortName;
        this.name = name;
    }

    public String getIndexId() {
        return indexId;
    }

    public void setIndexId(String indexId) {
        this.indexId = indexId;
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

    public Double getCurrentValue() {
        return currentValue;
    }

    public void setCurrentValue(String currentValue) {
        if (!StringUtils.isEmpty(currentValue)) {
            this.currentValue = Double.valueOf(currentValue);
        }
    }
}

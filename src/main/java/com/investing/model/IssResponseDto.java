package com.investing.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IssResponseDto {
    private int code;
    private Map<String, List<String>> headers = new HashMap<>();
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

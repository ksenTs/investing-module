package com.investing.rest;

import com.investing.model.IssResponseDto;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.function.Function;

public class IssHttpRestDriver {
    private String url;
    private CloseableHttpClient httpClient;

    public IssHttpRestDriver(String url, CloseableHttpClient httpClient) {
        this.url = url;
        this.httpClient = httpClient;
    }

    public <R> R get(String subUrl, Function<IssResponseDto, R> processor) throws IOException {
        HttpRequestBase httpRequest = new HttpGet(url + subUrl);
        return executeRequest(httpRequest, processor);
    }

    private <R> R executeRequest(HttpUriRequest httpUriRequest, Function<IssResponseDto, R> processor) throws IOException {
        IssResponseDto responseDto = new IssResponseDto();
        try (CloseableHttpResponse response = httpClient.execute(httpUriRequest)) {
            responseDto.setCode(response.getStatusLine().getStatusCode());
            if (response.getEntity() != null) {
                responseDto.setData(EntityUtils.toString(response.getEntity()));
                EntityUtils.consume(response.getEntity());
            }
        }
        return processor.apply(responseDto);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(CloseableHttpClient httpClient) {
        this.httpClient = httpClient;
    }
}

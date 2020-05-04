package com.investing.rest;

import com.investing.model.IssResponseDto;
import org.apache.http.HttpStatus;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class IssHttpRestClient {
    private IssHttpRestDriver restDriver;
    private static final String URL = "https://iss.moex.com/iss";

    @PostConstruct
    public void init() {
        CloseableHttpClient httpClient = HttpClients.custom().build();
        restDriver = new IssHttpRestDriver(URL, httpClient);
    }

    public IssHttpRestDriver getRestDriver() {
        return restDriver;
    }

    public IssResponseDto get(String subUrl) throws IOException {
        return restDriver.get(subUrl, this::checkResponse);
    }

    private IssResponseDto checkResponse(IssResponseDto responseDto) {
        if (HttpStatus.SC_OK != responseDto.getCode()) {
            throw new IllegalStateException("Request to ISS failed");
        }
        return responseDto;
    }

}

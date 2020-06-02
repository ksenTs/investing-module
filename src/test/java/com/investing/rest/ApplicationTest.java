package com.investing.rest;

import com.investing.model.IssResponseDto;
import com.investing.model.IssResultDto;
import com.investing.service.ExchangeToolService;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;


public class ApplicationTest {

    private static final String URL = "https://iss.moex.com/iss";
    private static final String GET_SHARES_LIST_URL = "/engines/stock/markets/shares/boards/TQBR/securities.json?iss.only=marketdata,securities";

    private IssHttpRestClient issHttpRestClient = new IssHttpRestClient();
    private ExchangeToolService exchangeToolService = new ExchangeToolService();


    @Before
    public void setUp() {
        IssHttpRestDriver restDriver = new IssHttpRestDriver(URL, HttpClients.custom().build());
        issHttpRestClient.setRestDriver(restDriver);
        exchangeToolService.setHttpRestClient(issHttpRestClient);
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void test_checkThatGetMethodReturnsData() throws IOException {
        IssResponseDto issResponseDto = issHttpRestClient.get(GET_SHARES_LIST_URL);
        assertNotNull(issResponseDto);
    }

    @Test(expected = IllegalStateException.class)
    public void test_checkThatGetMethodFailed() throws IOException {
        issHttpRestClient.get("INCORRECT_URL");
    }

    @Test
    public void test_checkThatExceptionHasRightMessage() throws IOException {
        exceptionRule.expect(IllegalStateException.class);
        exceptionRule.expectMessage("Request to ISS failed");
        issHttpRestClient.get("INCORRECT_URL");
    }

    @Test
    public void test_checkThatDeserializationReturnCorrectData() {
        assertEquals(exchangeToolService.getData(GET_SHARES_LIST_URL).getClass(), IssResultDto.class);
    }
}

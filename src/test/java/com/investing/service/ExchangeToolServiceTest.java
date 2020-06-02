package com.investing.service;

import com.investing.model.IssData;
import com.investing.model.IssResultDto;
import com.investing.rest.IssHttpRestDriver;
import org.apache.http.impl.client.HttpClients;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

public class ExchangeToolServiceTest {

    private static final String GET_SHARES_LIST_URL = "/engines/stock/markets/shares/boards/TQBR/securities.json?iss.only=marketdata,securities";

    @Mock
    private ExchangeToolService exchangeToolService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void test_checkThatDeserializationWorks() {
        IssResultDto<IssData> data = exchangeToolService.getData(GET_SHARES_LIST_URL);
        assertNotNull(data);
    }

}

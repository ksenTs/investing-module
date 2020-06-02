package com.investing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.investing.model.ExchangeTool;
import com.investing.model.ExchangeToolDetails;
import com.investing.model.ExchangeToolType;
import com.investing.model.IssCandles;
import com.investing.model.IssData;
import com.investing.model.IssResponseDto;
import com.investing.model.IssResultDto;
import com.investing.model.MarketDataCode;
import com.investing.model.PeriodValues;
import com.investing.model.SecuritiesCode;
import com.investing.rest.IssHttpRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.investing.rest.RequestBuilder.getPeriodAsString;

@Service
public class ExchangeToolService {

    private IssHttpRestClient httpRestClient;

    private static final ImmutableMap<MarketDataCode, Integer> CHART_DATA = ImmutableMap
            .<MarketDataCode, Integer> builder()
            .put(MarketDataCode.LOW, 3)
            .put(MarketDataCode.HIGH, 2)
            .put(MarketDataCode.BEGIN, 6)
            .build();


    @Autowired
    public void setHttpRestClient(IssHttpRestClient httpRestClient) {
        this.httpRestClient = httpRestClient;
    }

    public PeriodValues getPeriodData(String code, String period, ExchangeToolType type) {
        PeriodValues periodValues = new PeriodValues();
        IssCandles<IssData> chartData = getChartData("/engines/stock/markets/" + type.getCode() + "/boards/TQBR/securities/" + code + "/candles.json?" + getPeriodAsString(period));

        List<Double> low = new ArrayList<>();
        List<Double> high = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        chartData.getCandles().getData().forEach(l -> {
            low.add(Double.valueOf(l.get(CHART_DATA.get(MarketDataCode.LOW))));
            high.add(Double.valueOf(l.get(CHART_DATA.get(MarketDataCode.HIGH))));
            dates.add(l.get(CHART_DATA.get(MarketDataCode.BEGIN)));
        });

        periodValues.setLow(low);
        periodValues.setHigh(high);
        periodValues.setDates(dates);

        return periodValues;
    }

    public IssResultDto<IssData> getData(String url) {
        try {
            IssResponseDto responseDto = httpRestClient.get(url);
            return deserializeData(responseDto);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private IssCandles<IssData> getChartData(String url) {
        try {
            IssResponseDto responseDto = httpRestClient.get(url);
            return deserializeChartData(responseDto);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private IssResultDto<IssData> deserializeData(IssResponseDto responseDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseDto.getData(), new TypeReference<IssResultDto<IssData>>() {
        });
    }

    private IssCandles<IssData> deserializeChartData(IssResponseDto responseDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseDto.getData(), new TypeReference<IssCandles<IssData>>() {
        });
    }
}

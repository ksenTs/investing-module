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
import java.util.stream.Collectors;

import static com.investing.rest.RequestBuilder.getPeriodAsString;

@Service
public class ExchangeToolService {

    private IssHttpRestClient httpRestClient;

    private static final ImmutableMap<SecuritiesCode, Integer> INDEXES_SECURITIES = ImmutableMap
            .<SecuritiesCode, Integer> builder()
            .put(SecuritiesCode.SECID, 0)
            .put(SecuritiesCode.NAME, 2)
            .put(SecuritiesCode.SHORTNAME, 4)
            .build();

    private static final ImmutableMap<SecuritiesCode, Integer> SHARES_SECURITIES = ImmutableMap
            .<SecuritiesCode, Integer> builder()
            .put(SecuritiesCode.SECID, 0)
            .put(SecuritiesCode.SHORTNAME, 2)
            .build();

    private static final ImmutableMap<MarketDataCode, Integer> INDEXES_MARKET_DATA = ImmutableMap
            .<MarketDataCode, Integer> builder()
            .put(MarketDataCode.SECID, 0)
            .put(MarketDataCode.OPENVALUE, 3)
            .put(MarketDataCode.CURRENTVALUE, 4)
            .put(MarketDataCode.LASTCHANGE, 5)
            .put(MarketDataCode.MONTHLY_DELTA, 11)
            .put(MarketDataCode.YEARLY_DELTA, 12)
            .put(MarketDataCode.CAPITAL, 20)
            .put(MarketDataCode.HIGH, 22)
            .put(MarketDataCode.LOW, 23)
            .build();

    private static final ImmutableMap<MarketDataCode, Integer> SHARES_MARKET_DATA = ImmutableMap
            .<MarketDataCode, Integer> builder()
            .put(MarketDataCode.SECID, 0)
            .put(MarketDataCode.OPEN, 9)
            .put(MarketDataCode.LOW, 10)
            .put(MarketDataCode.HIGH, 11)
            .put(MarketDataCode.LAST, 12)
            .put(MarketDataCode.LASTCHANGE, 13)
            .put(MarketDataCode.VALUE, 16)
            .put(MarketDataCode.BID, 2)
            .build();

    @Autowired
    public void setHttpRestClient(IssHttpRestClient httpRestClient) {
        this.httpRestClient = httpRestClient;
    }

    public List<ExchangeTool> getIndexes() {
        IssResultDto<IssData> issData = getData("/engines/stock/markets/index/securities.json?iss.only=marketdata,securities");
        List<ExchangeTool> indexes = issData.getSecurities().getData().stream()
                .map(this::toIndex)
                .collect(Collectors.toList());
        indexes.forEach(indexDto -> issData.getMarketdata().getData().forEach(row -> {
            String code = row.get(INDEXES_MARKET_DATA.get(MarketDataCode.SECID));
            if (code.equals(indexDto.getCode())) {
                fillIndexMarketProperties(row, indexDto);
            }
        }));
        return indexes;
    }

    public List<ExchangeTool> getShares() {
        IssResultDto<IssData> issData = getData("/engines/stock/markets/shares/securities.json?iss.only=marketdata,securities");
        List<ExchangeTool> shares = issData.getSecurities().getData().stream()
                .map(this::toShare)
                .collect(Collectors.toList());
        shares.forEach(indexDto -> issData.getMarketdata().getData().forEach(row -> {
            String code = row.get(SHARES_MARKET_DATA.get(MarketDataCode.SECID));
            if (code.equals(indexDto.getCode())) {
                fillShareMarketProperties(row, indexDto);
            }
        }));
        return shares;
    }

    public ExchangeToolDetails getIndexDetails(String code) {
        IssResultDto<IssData> indexesData = getData("/engines/stock/markets/index/securities/" + code + ".json?iss.only=marketdata,securities");
        ExchangeTool baseDto = toIndex(indexesData.getSecurities().getData().get(0));
        fillIndexMarketProperties(indexesData.getMarketdata().getData().get(0), baseDto);
        return toExchangeToolDetails(indexesData.getMarketdata().getData().get(0), baseDto);
    }

    public PeriodValues getPeriodData(String code, String period) {
        PeriodValues periodValues = new PeriodValues();
        IssCandles<IssData> chartData = getChartData("/engines/stock/markets/index/boards/TQBR/securities/" + code + "/candles.json?" + getPeriodAsString(period));

        List<Double> low = new ArrayList<>();
        List<Double> high = new ArrayList<>();
        List<String> dates = new ArrayList<>();

        chartData.getCandles().getData().forEach(l -> {
            low.add(Double.valueOf(l.get(INDEXES_MARKET_DATA.get(MarketDataCode.LOW))));
            high.add(Double.valueOf(l.get(INDEXES_MARKET_DATA.get(MarketDataCode.HIGH))));
            dates.add(l.get(INDEXES_MARKET_DATA.get(MarketDataCode.BEGIN)));
        });

        periodValues.setLow(low);
        periodValues.setHigh(high);
        periodValues.setDates(dates);

        return periodValues;
    }

    private ExchangeTool toIndex(List<String> securitiesData) {
        ExchangeTool dto = new ExchangeToolDetails();
        dto.setCode(securitiesData.get(INDEXES_SECURITIES.get(SecuritiesCode.SECID)));
        dto.setName(securitiesData.get(INDEXES_SECURITIES.get(SecuritiesCode.NAME)));
        dto.setShortName(securitiesData.get(INDEXES_SECURITIES.get(SecuritiesCode.SHORTNAME)));
        return dto;
    }

    private ExchangeTool toShare(List<String> securitiesData) {
        ExchangeTool dto = new ExchangeToolDetails();
        dto.setCode(securitiesData.get(SHARES_SECURITIES.get(SecuritiesCode.SECID)));
        dto.setShortName(securitiesData.get(SHARES_SECURITIES.get(SecuritiesCode.SHORTNAME)));
        return dto;
    }

    private void fillIndexMarketProperties(List<String> marketData, ExchangeTool dto) {
        dto.setValue(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.CURRENTVALUE))));
        dto.setDelta(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.LASTCHANGE))));
    }

    private void fillShareMarketProperties(List<String> marketData, ExchangeTool dto) {
        dto.setValue(toDouble(marketData.get(SHARES_MARKET_DATA.get(MarketDataCode.LAST))));
    }

    private ExchangeToolDetails toExchangeToolDetails(List<String> marketData, ExchangeTool dto) {
        ExchangeToolDetails detailsDto = (ExchangeToolDetails) dto;
        detailsDto.setOpenValue(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.OPENVALUE))));
        detailsDto.setMonthlyDelta(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.MONTHLY_DELTA))));
        detailsDto.setYearlyDelta(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.YEARLY_DELTA))));
        detailsDto.setLowestValue(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.LOW))));
        detailsDto.setHighestValue(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.HIGH))));
        detailsDto.setCapitalization(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.CAPITAL))));
        return detailsDto;
    }

    private Double toDouble(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Double.valueOf(value);
        }
        return null;
    }

    private IssResultDto<IssData> getData(String url) {
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

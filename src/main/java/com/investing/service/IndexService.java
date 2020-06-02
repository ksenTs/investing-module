package com.investing.service;

import com.google.common.collect.ImmutableMap;
import com.investing.model.ApplicationConverter;
import com.investing.model.ExchangeTool;
import com.investing.model.ExchangeToolDetails;
import com.investing.model.IssData;
import com.investing.model.IssResultDto;
import com.investing.model.MarketDataCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.investing.model.ApplicationConverter.toDouble;
import static com.investing.model.ApplicationConverter.toIndex;
import static com.investing.rest.RequestBuilder.getIndexDetailsUrl;
import static com.investing.rest.RequestBuilder.getIndexesListUrl;

@Service
public class IndexService {

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

    private ExchangeToolService exchangeToolService;

    @Autowired
    public void setExchangeToolService(ExchangeToolService exchangeToolService) {
        this.exchangeToolService = exchangeToolService;
    }

    public List<ExchangeTool> getIndexes() {
        IssResultDto<IssData> issData = exchangeToolService.getData(getIndexesListUrl());
        List<ExchangeTool> indexes = issData.getSecurities().getData().stream()
                .map(ApplicationConverter::toIndex)
                .collect(Collectors.toList());
        indexes.forEach(indexDto -> issData.getMarketdata().getData().forEach(row -> {
            String code = row.get(INDEXES_MARKET_DATA.get(MarketDataCode.SECID));
            if (code.equals(indexDto.getCode())) {
                fillIndexMarketProperties(row, indexDto);
            }
        }));
        return indexes;
    }

    public ExchangeToolDetails getIndexDetails(String code) {
        IssResultDto<IssData> indexesData = exchangeToolService.getData(getIndexDetailsUrl(code));
        ExchangeTool baseDto = toIndex(indexesData.getSecurities().getData().get(0));
        fillIndexMarketProperties(indexesData.getMarketdata().getData().get(0), baseDto);
        return toExchangeToolDetails(indexesData.getMarketdata().getData().get(0), baseDto);
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

    private void fillIndexMarketProperties(List<String> marketData, ExchangeTool dto) {
        dto.setValue(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.CURRENTVALUE))));
        dto.setDelta(toDouble(marketData.get(INDEXES_MARKET_DATA.get(MarketDataCode.LASTCHANGE))));
    }
}

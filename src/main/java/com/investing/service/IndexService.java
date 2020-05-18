package com.investing.service;

import com.google.common.collect.ImmutableMap;
import com.investing.model.IndexDto;
import com.investing.model.MarketDataCode;
import com.investing.model.SecuritiesCode;
import com.investing.model.IssResponseDto;
import com.investing.model.IssResultDto;
import com.investing.model.IssData;
import com.investing.rest.IssHttpRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static com.investing.rest.Deserializer.deserializeData;

@Service
public class IndexService {

    private IssHttpRestClient httpRestClient;

    private static final ImmutableMap<SecuritiesCode, Integer> SECURITIES_CODE = ImmutableMap
            .<SecuritiesCode, Integer> builder()
            .put(SecuritiesCode.SECID, 0)
            .put(SecuritiesCode.NAME, 2)
            .put(SecuritiesCode.SHORTNAME, 4)
            .build();

    private static final ImmutableMap<MarketDataCode, Integer> MARKET_DATA_CODE = ImmutableMap
            .<MarketDataCode, Integer> builder()
            .put(MarketDataCode.CURRENTVALUE, 4)
            .put(MarketDataCode.SECID, 0)
            .put(MarketDataCode.LASTCHANGE, 5)
            .put(MarketDataCode.OPENVALUE, 3)
            .put(MarketDataCode.HIGH, 22)
            .put(MarketDataCode.LOW, 23)
            .put(MarketDataCode.MONTHLY_DELTA, 11)
            .put(MarketDataCode.YEARLY_DELTA, 12)
            .put(MarketDataCode.CAPITAL, 20)
            .build();

    @Autowired
    public void setHttpRestClient(IssHttpRestClient httpRestClient) {
        this.httpRestClient = httpRestClient;
    }

    public List<IndexDto> getIndexes() {
        IssResultDto<IssData> indexesData = getData("/engines/stock/markets/index/securities.json?iss.only=marketdata,securities");
        List<IndexDto> indexes = indexesData.getSecurities().getData().stream()
                .map(this::toBaseIndexDto)
                .collect(Collectors.toList());
        indexes.forEach(indexDto -> indexesData.getMarketdata().getData().forEach(row -> {
            String code = row.get(MARKET_DATA_CODE.get(MarketDataCode.SECID));
            if (code.equals(indexDto.getCode())) {
                fillExtendedProperties(row, indexDto);
            }
        }));
        return indexes;
    }

    private IndexDto toBaseIndexDto(List<String> securitiesData) {
        IndexDto dto = new IndexDto();
        dto.setCode(securitiesData.get(SECURITIES_CODE.get(SecuritiesCode.SECID)));
        dto.setName(securitiesData.get(SECURITIES_CODE.get(SecuritiesCode.NAME)));
        dto.setShortName(securitiesData.get(SECURITIES_CODE.get(SecuritiesCode.SHORTNAME)));
        return dto;
    }

    private void fillExtendedProperties(List<String> marketData, IndexDto dto) {
        dto.setValue(toDouble(marketData.get(MARKET_DATA_CODE.get(MarketDataCode.CURRENTVALUE))));
        dto.setDelta(toDouble(marketData.get(MARKET_DATA_CODE.get(MarketDataCode.LASTCHANGE))));
        dto.setOpenValue(toDouble(marketData.get(MARKET_DATA_CODE.get(MarketDataCode.OPENVALUE))));
        dto.setMonthlyDelta(toDouble(marketData.get(MARKET_DATA_CODE.get(MarketDataCode.MONTHLY_DELTA))));
        dto.setYearlyDelta(toDouble(marketData.get(MARKET_DATA_CODE.get(MarketDataCode.YEARLY_DELTA))));
        dto.setLowestValue(toDouble(marketData.get(MARKET_DATA_CODE.get(MarketDataCode.LOW))));
        dto.setHighestValue(toDouble(marketData.get(MARKET_DATA_CODE.get(MarketDataCode.HIGH))));
        dto.setCapitalization(toDouble(marketData.get(MARKET_DATA_CODE.get(MarketDataCode.CAPITAL))));
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

}

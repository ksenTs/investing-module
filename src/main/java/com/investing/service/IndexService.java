package com.investing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
            .build();

    @Autowired
    public void setHttpRestClient(IssHttpRestClient httpRestClient) {
        this.httpRestClient = httpRestClient;
    }

    public List<IndexDto> getIndexes() {
        List<IndexDto> indexes;
        try {
            IssResponseDto responseDto = httpRestClient.get("/engines/stock/markets/index/securities.json?iss.only=marketdata,securities");
            IssResultDto<IssData> sharesData = deserializeData(responseDto);
            indexes = sharesData.getSecurities().getData().stream()
                    .map(l -> new IndexDto(
                            l.get(SECURITIES_CODE.get(SecuritiesCode.SECID)),
                            l.get(SECURITIES_CODE.get(SecuritiesCode.SHORTNAME)),
                            l.get(SECURITIES_CODE.get(SecuritiesCode.NAME))
                            ))
                    .collect(Collectors.toList());
            indexes.forEach(i -> sharesData.getMarketdata().getData().forEach(row -> {
                if (row.get(MARKET_DATA_CODE.get(MarketDataCode.SECID)).equals(i.getIndexId())) {
                    i.setCurrentValue(row.get(MARKET_DATA_CODE.get(MarketDataCode.CURRENTVALUE)));
                }
            }));

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return indexes;
    }


}

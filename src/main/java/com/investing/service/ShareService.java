package com.investing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.investing.model.IssCandles;
import com.investing.model.IssCode;
import com.investing.model.IssResponseDto;
import com.investing.model.IssResultDto;
import com.investing.model.MonthlyValues;
import com.investing.model.ShareDto;
import com.investing.model.ShareIssData;
import com.investing.rest.IssHttpRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.investing.model.ShareConverter.toShareDto;

@Service
public class ShareService {

    private IssHttpRestClient httpRestClient;

    private static final ImmutableMap<IssCode, Integer> CODE_POSITION_MAP = ImmutableMap
            .<IssCode, Integer> builder()
            .put(IssCode.SECID, 0)
            .put(IssCode.LAST, 12)
            .put(IssCode.HIGH, 2)
            .put(IssCode.LOW, 3)
            .put(IssCode.BEGIN, 6)
            .build();

    @Autowired
    public void setHttpRestClient(IssHttpRestClient httpRestClient) {
        this.httpRestClient = httpRestClient;
    }

    public List<ShareDto> getShares() {
        List<ShareDto> shares;
        try {
            IssResponseDto responseDto = httpRestClient.get("/engines/stock/markets/shares/boards/tqbr/securities.json?iss.only=marketdata&iss.meta=off");
            IssResultDto<ShareIssData> sharesData = deserializeData(responseDto);
            shares = sharesData.getMarketdata().getData().stream()
                    .map(l -> toShareDto(l.get(CODE_POSITION_MAP.get(IssCode.SECID)), l.get(CODE_POSITION_MAP.get(IssCode.LAST))))
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return shares;
    }

    public MonthlyValues getMonthlyValues() {
        MonthlyValues monthlyValues = new MonthlyValues();
        try {
            IssResponseDto responseDto = httpRestClient.get("/engines/stock/markets/shares/boards/TQBR/securities/SBER/candles.json?from=2020-04-01&till=2020-05-02&interval=24&iss.meta=off");
            ObjectMapper mapper = new ObjectMapper();
            IssCandles<ShareIssData> sharesData = mapper.readValue(responseDto.getData(), new TypeReference<IssCandles<ShareIssData>>() {
            });
            List<Double> low = new ArrayList<>();
            List<Double> high = new ArrayList<>();
            List<String> dates = new ArrayList<>();

            sharesData.getCandles().getData().forEach(l -> {
                low.add(Double.valueOf(l.get(CODE_POSITION_MAP.get(IssCode.LOW))));
                high.add(Double.valueOf(l.get(CODE_POSITION_MAP.get(IssCode.HIGH))));
                dates.add(l.get(CODE_POSITION_MAP.get(IssCode.BEGIN)));
            });
            monthlyValues.setLow(low);
            monthlyValues.setHigh(high);
            monthlyValues.setDates(dates);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return monthlyValues;
    }

    private IssResultDto<ShareIssData> deserializeData(IssResponseDto responseDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseDto.getData(), new TypeReference<IssResultDto<ShareIssData>>() {
        });
    }

}

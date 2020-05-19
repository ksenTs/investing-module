package com.investing.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.investing.model.ExchangeTool;
import com.investing.model.IssCandles;
import com.investing.model.MarketDataCode;
import com.investing.model.IssResponseDto;
import com.investing.model.IssResultDto;
import com.investing.model.PeriodValues;
import com.investing.model.ShareDto;
import com.investing.model.IssData;
import com.investing.rest.IssHttpRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.investing.rest.Deserializer.deserializeData;
import static com.investing.rest.RequestBuilder.getPeriodAsString;

@Service
public class ShareService {

    private IssHttpRestClient httpRestClient;

    private static final ImmutableMap<MarketDataCode, Integer> CODE_POSITION_MAP = ImmutableMap
            .<MarketDataCode, Integer> builder()
            .put(MarketDataCode.SECID, 0)
            .put(MarketDataCode.LAST, 12)
            .put(MarketDataCode.HIGH, 2)
            .put(MarketDataCode.LOW, 3)
            .put(MarketDataCode.BEGIN, 6)
            .build();

    @Autowired
    public void setHttpRestClient(IssHttpRestClient httpRestClient) {
        this.httpRestClient = httpRestClient;
    }

//    public List<ShareDto> getShares() {
////        List<ShareDto> shares;
////        try {
////            IssResponseDto responseDto = httpRestClient.get("/engines/stock/markets/shares/boards/tqbr/securities.json?iss.only=marketdata&iss.meta=off");
////            IssResultDto<IssData> sharesData = deserializeData(responseDto);
////            shares = sharesData.getMarketdata().getData().stream()
////                    .map(l -> toShareDto(l.get(CODE_POSITION_MAP.get(MarketDataCode.SECID)), l.get(CODE_POSITION_MAP.get(MarketDataCode.LAST))))
////                    .collect(Collectors.toList());
////
////        } catch (IOException e) {
////            throw new IllegalStateException(e);
////        }
////        return shares;
////    }


    public PeriodValues getMonthlyValues(String code, String period) {
        PeriodValues monthlyValues = new PeriodValues();
        try {
            String check = getPeriodAsString(period);
            IssResponseDto responseDto = httpRestClient.get("/engines/stock/markets/shares/boards/TQBR/securities/" + code + "/candles.json?" + check);
            ObjectMapper mapper = new ObjectMapper();
            IssCandles<IssData> sharesData = mapper.readValue(responseDto.getData(), new TypeReference<IssCandles<IssData>>() {
            });
            List<Double> low = new ArrayList<>();
            List<Double> high = new ArrayList<>();
            List<String> dates = new ArrayList<>();

            sharesData.getCandles().getData().forEach(l -> {
                low.add(Double.valueOf(l.get(CODE_POSITION_MAP.get(MarketDataCode.LOW))));
                high.add(Double.valueOf(l.get(CODE_POSITION_MAP.get(MarketDataCode.HIGH))));
                dates.add(l.get(CODE_POSITION_MAP.get(MarketDataCode.BEGIN)));
            });
            monthlyValues.setLow(low);
            monthlyValues.setHigh(high);
            monthlyValues.setDates(dates);

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        return monthlyValues;
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

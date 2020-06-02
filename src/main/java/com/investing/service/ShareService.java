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

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.investing.model.ApplicationConverter.toDouble;
import static com.investing.model.ApplicationConverter.toShare;


@Service
public class ShareService {

    private static final String SHARES_LIST_URL = "/engines/stock/markets/shares/boards/TQBR/securities.json?iss.only=marketdata,securities";
    private static final String SHARES_DETAILS_URL = "/engines/stock/markets/shares/boards/TQBR/securities.json?iss.only=marketdata,securities";
    private ExchangeToolService exchangeToolService;

    @Autowired
    public void setExchangeToolService(ExchangeToolService exchangeToolService) {
        this.exchangeToolService = exchangeToolService;
    }

    private static final ImmutableMap<MarketDataCode, Integer> SHARES_MARKET_DATA = ImmutableMap
            .<MarketDataCode, Integer> builder()
            .put(MarketDataCode.SECID, 0)
            .put(MarketDataCode.OPEN, 9)
            .put(MarketDataCode.LOW, 10)
            .put(MarketDataCode.HIGH, 11)
            .put(MarketDataCode.LAST, 12)
            .put(MarketDataCode.LASTCHANGE, 13)
            .put(MarketDataCode.VALUE, 12)
            .put(MarketDataCode.BID, 2)
            .put(MarketDataCode.DELTA, 41)
            .build();

    public List<ExchangeTool> getShares() {
        IssResultDto<IssData> issData = exchangeToolService.getData(SHARES_LIST_URL);
        List<ExchangeTool> shares = issData.getSecurities().getData().stream()
                .map(ApplicationConverter::toShare)
                .collect(Collectors.toList());
        shares.forEach(indexDto -> issData.getMarketdata().getData().forEach(row -> {
            String code = row.get(SHARES_MARKET_DATA.get(MarketDataCode.SECID));
            if (code.equals(indexDto.getCode())) {
                fillShareMarketProperties(row, indexDto);
            }
        }));
        return shares;
    }

    public ExchangeToolDetails getShareDetails(String code) {
        IssResultDto<IssData> sharesData = exchangeToolService.getData("/engines/stock/markets/shares/securities/" + code + ".json?iss.only=marketdata,securities");
        ExchangeTool baseDto = toShare(sharesData.getSecurities().getData().get(0));
        fillShareMarketProperties(sharesData.getMarketdata().getData().get(0), baseDto);
        return toExchangeToolDetails(sharesData.getMarketdata().getData().get(0), baseDto);
    }

    private ExchangeToolDetails toExchangeToolDetails(List<String> marketData, ExchangeTool dto) {
        ExchangeToolDetails detailsDto = (ExchangeToolDetails) dto;
        detailsDto.setOpenValue(toDouble(marketData.get(SHARES_MARKET_DATA.get(MarketDataCode.OPENVALUE))));
        detailsDto.setMonthlyDelta(toDouble(marketData.get(SHARES_MARKET_DATA.get(MarketDataCode.MONTHLY_DELTA))));
        detailsDto.setYearlyDelta(toDouble(marketData.get(SHARES_MARKET_DATA.get(MarketDataCode.YEARLY_DELTA))));
        detailsDto.setLowestValue(toDouble(marketData.get(SHARES_MARKET_DATA.get(MarketDataCode.LOW))));
        detailsDto.setHighestValue(toDouble(marketData.get(SHARES_MARKET_DATA.get(MarketDataCode.HIGH))));
        return detailsDto;
    }

    public List<ExchangeTool> getSharesGrowthLeaders() {
        return getShares().stream()
                .filter(t -> t.getDelta() != null)
                .sorted(Comparator.comparing(ExchangeTool::getDelta))
                .collect(Collectors.toList());
    }

    public List<ExchangeTool> getSharesFallLeaders() {
        return getShares().stream()
                .filter(t -> t.getDelta() != null)
                .sorted(Comparator.comparing(ExchangeTool::getDelta).reversed())
                .collect(Collectors.toList());
    }

    private void fillShareMarketProperties(List<String> marketData, ExchangeTool dto) {
        dto.setValue(toDouble(marketData.get(SHARES_MARKET_DATA.get(MarketDataCode.VALUE))));
        dto.setDelta(toDouble(marketData.get(SHARES_MARKET_DATA.get(MarketDataCode.DELTA))));
    }
}

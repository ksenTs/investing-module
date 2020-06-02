package com.investing.model;

import com.google.common.collect.ImmutableMap;
import org.springframework.util.StringUtils;

import java.util.List;

public class ApplicationConverter {

    private ApplicationConverter() {}

    private static final ImmutableMap<SecuritiesCode, Integer> SHARES_SECURITIES = ImmutableMap
            .<SecuritiesCode, Integer> builder()
            .put(SecuritiesCode.SECID, 0)
            .put(SecuritiesCode.SHORTNAME, 2)
            .build();

    private static final ImmutableMap<SecuritiesCode, Integer> INDEXES_SECURITIES = ImmutableMap
            .<SecuritiesCode, Integer> builder()
            .put(SecuritiesCode.SECID, 0)
            .put(SecuritiesCode.NAME, 2)
            .put(SecuritiesCode.SHORTNAME, 4)
            .build();

    public static ExchangeTool toShare(List<String> securitiesData) {
        ExchangeTool dto = new ExchangeToolDetails();
        dto.setCode(securitiesData.get(SHARES_SECURITIES.get(SecuritiesCode.SECID)));
        dto.setShortName(securitiesData.get(SHARES_SECURITIES.get(SecuritiesCode.SHORTNAME)));
        return dto;
    }

    public static ExchangeTool toIndex(List<String> securitiesData) {
        ExchangeTool dto = new ExchangeToolDetails();
        dto.setCode(securitiesData.get(INDEXES_SECURITIES.get(SecuritiesCode.SECID)));
        dto.setName(securitiesData.get(INDEXES_SECURITIES.get(SecuritiesCode.NAME)));
        dto.setShortName(securitiesData.get(INDEXES_SECURITIES.get(SecuritiesCode.SHORTNAME)));
        return dto;
    }

    public static Double toDouble(String value) {
        if (!StringUtils.isEmpty(value)) {
            return Double.valueOf(value);
        }
        return null;
    }

}

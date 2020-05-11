package com.investing.model;

import java.util.Objects;

public class ApplicationConverter {
    private ApplicationConverter() {}

    public static ShareDto toShareDto(String code, String cost) {
        ShareDto dto = new ShareDto();
        dto.setCode(code);
        if (!Objects.isNull(cost)) {
            dto.setCost(Double.valueOf(cost));
        }
        return dto;
    }
}

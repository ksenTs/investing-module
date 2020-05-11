package com.investing.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.investing.model.IssData;
import com.investing.model.IssResponseDto;
import com.investing.model.IssResultDto;

public class Deserializer {

    private Deserializer() {}

    public static IssResultDto<IssData> deserializeData(IssResponseDto responseDto) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(responseDto.getData(), new TypeReference<IssResultDto<IssData>>() {
        });
    }

}

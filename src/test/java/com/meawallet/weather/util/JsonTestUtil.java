package com.meawallet.weather.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meawallet.weather.model.ErrorDto;
import com.meawallet.weather.model.WeatherResponseDto;

public class JsonTestUtil {

    public static WeatherResponseDto jsonToWeatherResponseDto(String json) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(json, WeatherResponseDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static ErrorDto jsonToErrorDto(String json) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            return mapper.readValue(json, ErrorDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}

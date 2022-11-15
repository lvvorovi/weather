package com.meawallet.weather.deserializer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meawallet.weather.deserializer.WeatherApiDtoDeserializer;
import com.meawallet.weather.handler.exception.WeatherApiDtoDeserializerException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.util.WeatherDeserializerUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.buildDeserializerFailMessage;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.buildDeserializerNullResponseMessage;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.buildYrDeserializerEndMessage;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.buildYrDeserializerStartMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class WeatherYrDtoDeserializer implements WeatherApiDtoDeserializer {

    private final CustomStdDeserializer deserializer;
    private final WeatherDeserializerUtil util;

    @Override
    public WeatherApiDto deserializeApiResponse(String jsonString) {
        ObjectMapper mapper = util.getWeatherObjectMapper(deserializer);
        WeatherApiDto apiDto;
        log.info(buildYrDeserializerStartMessage(jsonString));

        try {
            apiDto = mapper.readValue(jsonString, WeatherApiDto.class);
        } catch (JsonProcessingException e) {
            throw new WeatherApiDtoDeserializerException(buildDeserializerFailMessage(e.getMessage()));
        }

        if (apiDto == null) {
            throw new WeatherApiDtoDeserializerException(buildDeserializerNullResponseMessage(jsonString));
        }

        log.info(buildYrDeserializerEndMessage(apiDto.toString()));
        return apiDto;
    }
}

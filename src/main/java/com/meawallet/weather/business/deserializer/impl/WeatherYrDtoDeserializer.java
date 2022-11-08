package com.meawallet.weather.business.deserializer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meawallet.weather.business.deserializer.WeatherApiDtoDeserializer;
import com.meawallet.weather.business.util.WeatherDeserializerUtil;
import com.meawallet.weather.handler.exception.WeatherApiDtoDeserializerException;
import com.meawallet.weather.model.WeatherApiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.business.message.store.WeatherDeserializerMessageStore.buildDeserializerFailMessage;
import static com.meawallet.weather.business.message.store.WeatherDeserializerMessageStore.buildDeserializerNullResponseMessage;
import static com.meawallet.weather.business.message.store.WeatherDeserializerMessageStore.buildYrDeserializerEndMessage;
import static com.meawallet.weather.business.message.store.WeatherDeserializerMessageStore.buildYrDeserializerStartMessage;

@Component
@Slf4j
@RequiredArgsConstructor
public class WeatherYrDtoDeserializer implements WeatherApiDtoDeserializer {

    private final CustomStdDeserializer customStdDeserializer;
    private final WeatherDeserializerUtil util;

    @Override
    public WeatherApiDto deserializeApiResponse(String jsonString) {
        ObjectMapper mapper = util.getWeatherObjectMapper(customStdDeserializer);
        WeatherApiDto apiDto;

        if (log.isDebugEnabled()) {
            log.debug(buildYrDeserializerStartMessage(jsonString));
        }

        try {
            apiDto = mapper.readValue(jsonString, WeatherApiDto.class);
        } catch (JsonProcessingException e) {
            throw new WeatherApiDtoDeserializerException(buildDeserializerFailMessage(e.getMessage()));
        }

        if (apiDto == null) {
            throw new WeatherApiDtoDeserializerException(buildDeserializerNullResponseMessage(jsonString));
        }

        if (log.isDebugEnabled()) {
            log.debug(buildYrDeserializerEndMessage(apiDto.toString()));
        }

        return apiDto;
    }
}

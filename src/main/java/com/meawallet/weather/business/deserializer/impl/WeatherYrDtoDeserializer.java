package com.meawallet.weather.business.deserializer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meawallet.weather.business.deserializer.WeatherApiDtoDeserializer;
import com.meawallet.weather.business.handler.exception.WeatherApiDtoDeserializerException;
import com.meawallet.weather.business.util.WeatherDeserializerUtil;
import com.meawallet.weather.model.WeatherApiDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.business.ConstantsStore.DESERIALIZER_DESERIALIZED_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.DESERIALIZER_FAIL_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.DESERIALIZER_NULL_MESSAGE;

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

        try {
            apiDto = mapper.readValue(jsonString, WeatherApiDto.class);
        } catch (JsonProcessingException e) {
            throw new WeatherApiDtoDeserializerException(String
                    .format(DESERIALIZER_FAIL_MESSAGE, e.getMessage()));
        }

        if (apiDto == null) {
            throw new WeatherApiDtoDeserializerException(DESERIALIZER_NULL_MESSAGE);
        }

        if (log.isDebugEnabled()) {
            log.debug(String.format(DESERIALIZER_DESERIALIZED_MESSAGE, jsonString, apiDto));
        }

        return apiDto;
    }
}

package com.meawallet.weather.validation.rule.impl;

import com.meawallet.weather.handler.exception.WeatherApiDtoValidationException;
import com.meawallet.weather.message.store.WeatherValidationMessageStore;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.validation.rule.WeatherApiDtoValidationRule;
import org.springframework.stereotype.Component;

@Component
public class WeatherApiDtoLongitudeValidationRule implements WeatherApiDtoValidationRule {

    @Override
    public void validate(WeatherApiDto dto) {
        if (dto.getLon() == null) {
            throw new WeatherApiDtoValidationException(WeatherValidationMessageStore.WEATHER_API_LONGITUDE_NULL_MESSAGE);
        }
    }
}

package com.meawallet.weather.business.validation.rule.impl;

import com.meawallet.weather.handler.exception.WeatherApiDtoValidationException;
import com.meawallet.weather.business.validation.rule.WeatherApiDtoValidationRule;
import com.meawallet.weather.model.WeatherApiDto;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.message.store.WeatherValidationMessageStore.WEATHER_API_LONGITUDE_NULL_MESSAGE;

@Component
public class LongitudeWeatherApiDtoValidationRule implements WeatherApiDtoValidationRule {

    @Override
    public void validate(WeatherApiDto dto) {
        if (dto.getLon() == null) {
            throw new WeatherApiDtoValidationException(WEATHER_API_LONGITUDE_NULL_MESSAGE);
        }
    }
}

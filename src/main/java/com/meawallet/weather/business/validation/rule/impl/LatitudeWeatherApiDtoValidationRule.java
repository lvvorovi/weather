package com.meawallet.weather.business.validation.rule.impl;

import com.meawallet.weather.business.handler.exception.WeatherApiDtoValidationException;
import com.meawallet.weather.business.validation.rule.WeatherApiDtoValidationRule;
import com.meawallet.weather.model.WeatherApiDto;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.business.ConstantsStore.WEATHER_API_LATITUDE_NULL_MESSAGE;

@Component
public class LatitudeWeatherApiDtoValidationRule implements WeatherApiDtoValidationRule {

    @Override
    public void validate(WeatherApiDto dto) {
        if (dto.getLat() == null) {
            throw new WeatherApiDtoValidationException(WEATHER_API_LATITUDE_NULL_MESSAGE);
        }
    }
}

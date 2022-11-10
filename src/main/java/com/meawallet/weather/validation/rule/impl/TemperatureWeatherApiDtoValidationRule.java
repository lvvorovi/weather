package com.meawallet.weather.validation.rule.impl;

import com.meawallet.weather.validation.rule.WeatherApiDtoValidationRule;
import com.meawallet.weather.handler.exception.WeatherApiDtoValidationException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.message.store.WeatherValidationMessageStore;
import org.springframework.stereotype.Component;

@Component
public class TemperatureWeatherApiDtoValidationRule implements WeatherApiDtoValidationRule {

    @Override
    public void validate(WeatherApiDto dto) {
        if (dto.getTemperature() == null) {
            throw new WeatherApiDtoValidationException(WeatherValidationMessageStore.WEATHER_API_TEMPERATURE_NULL_MESSAGE);
        }
    }
}

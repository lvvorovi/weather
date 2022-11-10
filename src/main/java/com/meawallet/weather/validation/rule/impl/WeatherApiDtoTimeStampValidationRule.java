package com.meawallet.weather.validation.rule.impl;

import com.meawallet.weather.handler.exception.WeatherApiDtoValidationException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.validation.rule.WeatherApiDtoValidationRule;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.message.store.WeatherValidationMessageStore.WEATHER_API_TIMESTAMP_NULL_MESSAGE;

@Component
public class WeatherApiDtoTimeStampValidationRule implements WeatherApiDtoValidationRule {

    @Override
    public void validate(WeatherApiDto dto) {
        if (dto.getTimeStamp() == null) {
            throw new WeatherApiDtoValidationException(WEATHER_API_TIMESTAMP_NULL_MESSAGE);
        }
    }
}

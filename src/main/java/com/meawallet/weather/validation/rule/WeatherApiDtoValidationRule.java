package com.meawallet.weather.validation.rule;

import com.meawallet.weather.model.WeatherApiDto;

public interface WeatherApiDtoValidationRule {

    void validate(WeatherApiDto dto);

}

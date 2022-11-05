package com.meawallet.weather.business.validation.rule;

import com.meawallet.weather.model.WeatherApiDto;

public interface WeatherApiDtoValidationRule {

    void validate(WeatherApiDto dto);

}

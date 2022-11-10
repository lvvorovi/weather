package com.meawallet.weather.validation.service;

import com.meawallet.weather.model.WeatherApiDto;

public interface WeatherValidationService {

    void validate(WeatherApiDto weatherApiDto);
}

package com.meawallet.weather.business.validation.service;

import com.meawallet.weather.model.WeatherApiDto;

public interface WeatherValidationService {

    void validate(WeatherApiDto createDto);
}

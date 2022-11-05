package com.meawallet.weather.business.service;

import com.meawallet.weather.model.WeatherApiDto;

public interface WeatherApiService {
    WeatherApiDto getByLatAndLonAndAlt(Float lat, Float lon, Integer alt);
}

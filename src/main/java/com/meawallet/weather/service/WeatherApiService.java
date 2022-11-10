package com.meawallet.weather.service;

import com.meawallet.weather.model.WeatherApiDto;

public interface WeatherApiService {

    WeatherApiDto getByLatAndLonAndAlt(Float lat, Float lon, Integer alt);
}

package com.meawallet.weather.service;

import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;

public interface WeatherService {

    WeatherResponseDto findDtoByLatAndLonAndAlt(Float lat, Float lon, Integer altitude);

    WeatherResponseDto save(WeatherApiDto requestDto);

    void deleteOutdated();
}

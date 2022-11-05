package com.meawallet.weather.business.service;

import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;

public interface WeatherService {


    WeatherResponseDto findByLatAndLonAndAlt(Float lat, Float lon, Integer altitude);

    WeatherResponseDto save(WeatherApiDto requestDto);

    void deleteOutdated();
}

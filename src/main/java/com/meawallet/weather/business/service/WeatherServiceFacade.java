package com.meawallet.weather.business.service;

import com.meawallet.weather.model.WeatherResponseDto;

public interface WeatherServiceFacade {

    WeatherResponseDto findByLatAndLonAndAlt(Float lat, Float lon, Integer altitude);

}

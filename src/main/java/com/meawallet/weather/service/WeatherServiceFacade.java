package com.meawallet.weather.service;

import com.meawallet.weather.model.WeatherResponseDto;
import org.springframework.security.access.prepost.PreAuthorize;

public interface WeatherServiceFacade {

//    @PreAuthorize("isAuthenticated()")
    WeatherResponseDto findByLatAndLonAndAlt(Float lat, Float lon, Integer altitude);

}
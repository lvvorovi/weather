package com.meawallet.weather.web.controller.impl;

import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.service.WeatherServiceFacade;
import com.meawallet.weather.web.controller.WeatherController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherControllerImpl implements WeatherController {

    private final WeatherServiceFacade service;

    @Override
    public ResponseEntity<WeatherResponseDto> findByLatAndLonAndAlt(Float lat, Float lon, Integer altitude) {
        WeatherResponseDto response = service.findByLatAndLonAndAlt(lat, lon, altitude);
        return ResponseEntity.ok().body(response);
    }
}

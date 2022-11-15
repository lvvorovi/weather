package com.meawallet.weather.web.controller.impl;

import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.service.WeatherServiceFacade;
import com.meawallet.weather.web.controller.WeatherController;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/v1/weather")
@Validated
public class WeatherControllerImpl implements WeatherController {

    private final WeatherServiceFacade service;

    @Qualifier("WeatherFindLatLonAltCounter")
    @NotNull
    private final Counter findByLatAndLonAndAltCounter;

    public WeatherControllerImpl(WeatherServiceFacade service, Counter findByLatAndLonAndAltCounter) {
        this.service = service;
        this.findByLatAndLonAndAltCounter = findByLatAndLonAndAltCounter;
    }

    @Override
    @Timed("WeatherController.findByLatAndLonAndAlt")
    public ResponseEntity<WeatherResponseDto> findByLatAndLonAndAlt(Float lat, Float lon, Integer altitude) {
        findByLatAndLonAndAltCounter.increment();
        WeatherResponseDto response = service.findByLatAndLonAndAlt(lat, lon, altitude);
        return ResponseEntity.ok().body(response);
    }
}

package com.meawallet.weather.controller.impl;

import com.meawallet.weather.business.service.WeatherServiceFacade;
import com.meawallet.weather.controller.WeatherController;
import com.meawallet.weather.model.WeatherResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherControllerImpl implements WeatherController {

    private final WeatherServiceFacade service;


    @Override
    public ResponseEntity<WeatherResponseDto> findByLatAndLonAndAlt(Float lat, Float lon, Integer altitude) {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        WeatherResponseDto response = service.findByLatAndLonAndAlt(lat, lon, altitude);
        return ResponseEntity.ok().body(response);
    }

}

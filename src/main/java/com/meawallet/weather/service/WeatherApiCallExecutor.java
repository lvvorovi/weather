package com.meawallet.weather.service;

import com.meawallet.weather.payload.YrApiServiceRequestDto;
import org.springframework.http.ResponseEntity;

public interface WeatherApiCallExecutor {

    ResponseEntity<String> execute(YrApiServiceRequestDto requestDto, Class<String> stringClass);

}

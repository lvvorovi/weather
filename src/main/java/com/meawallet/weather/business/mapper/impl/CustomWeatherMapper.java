package com.meawallet.weather.business.mapper.impl;

import com.meawallet.weather.business.mapper.WeatherMapper;
import com.meawallet.weather.business.repository.entity.WeatherEntity;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import org.springframework.stereotype.Component;

@Component
public class CustomWeatherMapper implements WeatherMapper {

    @Override
    public WeatherResponseDto entityToDto(WeatherEntity entity) {
        WeatherResponseDto responseDto = new WeatherResponseDto();
        responseDto.setTemperature(entity.getTemperature());

        return responseDto;
    }

    @Override
    public WeatherEntity dtoToEntity(WeatherApiDto requestDto) {
        WeatherEntity entity = new WeatherEntity();
        entity.setTimeStamp(requestDto.getTimeStamp());
        entity.setLat(requestDto.getLat());
        entity.setLon(requestDto.getLon());
        entity.setAltitude(requestDto.getAltitude());
        entity.setTemperature(requestDto.getTemperature());

        return entity;
    }
}

package com.meawallet.weather.mapper;

import com.meawallet.weather.repository.entity.WeatherEntity;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;

public interface WeatherMapper {

    WeatherResponseDto entityToDto(WeatherEntity entity);

    WeatherEntity dtoToEntity(WeatherApiDto createDto);
}

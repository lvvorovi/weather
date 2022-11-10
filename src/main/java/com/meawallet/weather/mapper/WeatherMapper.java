package com.meawallet.weather.mapper;

import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.repository.entity.WeatherEntity;

public interface WeatherMapper {

    WeatherResponseDto entityToDto(WeatherEntity entity);

    WeatherEntity dtoToEntity(WeatherApiDto createDto);
}

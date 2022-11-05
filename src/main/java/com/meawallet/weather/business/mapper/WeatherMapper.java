package com.meawallet.weather.business.mapper;

import com.meawallet.weather.business.repository.entity.WeatherEntity;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;

public interface WeatherMapper {

    WeatherResponseDto entityToDto(WeatherEntity entity);

    WeatherEntity dtoToEntity(WeatherApiDto createDto);
}

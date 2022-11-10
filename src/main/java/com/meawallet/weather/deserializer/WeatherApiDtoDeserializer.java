package com.meawallet.weather.deserializer;

import com.meawallet.weather.model.WeatherApiDto;

public interface WeatherApiDtoDeserializer {
    WeatherApiDto deserializeApiResponse(String response);

}

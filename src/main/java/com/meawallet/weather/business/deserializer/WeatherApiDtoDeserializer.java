package com.meawallet.weather.business.deserializer;

import com.meawallet.weather.model.WeatherApiDto;

public interface WeatherApiDtoDeserializer {
    WeatherApiDto deserializeApiResponse(String response);

}

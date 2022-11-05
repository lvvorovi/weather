package com.meawallet.weather.business.deserializer;

import com.meawallet.weather.model.WeatherApiDto;
import org.springframework.stereotype.Component;

@Component
public interface WeatherApiDtoDeserializer {
    WeatherApiDto deserializeApiResponse(String response);

}

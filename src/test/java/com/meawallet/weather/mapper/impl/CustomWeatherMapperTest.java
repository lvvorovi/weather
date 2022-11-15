package com.meawallet.weather.mapper.impl;

import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.repository.entity.WeatherEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.meawallet.weather.test.util.WeatherTestUtil.weatherApiDto;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class CustomWeatherMapperTest {

    @InjectMocks
    CustomWeatherMapper victim;

    @Test
    void entityToDto_whenEntity_returnDto() {
        WeatherEntity expected = weatherEntity();

        WeatherResponseDto result = victim.entityToDto(expected);

        assertEquals(expected.getTemperature(), result.temperature());
    }

    @Test
    void dtoToEntity_whenDto_thenReturnEntity() {
        WeatherApiDto expected = weatherApiDto();

        WeatherEntity result = victim.dtoToEntity(expected);

        assertEquals(expected.getAltitude(), result.getAltitude());
        assertEquals(expected.getLon(), result.getLon());
        assertEquals(expected.getLat(), result.getLat());
        assertEquals(expected.getTimeStamp(), result.getTimeStamp());
        assertEquals(expected.getTemperature(), result.getTemperature());
        assertNull(result.getId());
    }

}

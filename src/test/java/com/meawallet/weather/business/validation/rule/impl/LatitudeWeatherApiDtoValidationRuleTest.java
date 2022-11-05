package com.meawallet.weather.business.validation.rule.impl;

import com.meawallet.weather.business.handler.exception.WeatherApiDtoValidationException;
import com.meawallet.weather.model.WeatherApiDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.meawallet.weather.business.ConstantsStore.WEATHER_API_LATITUDE_NULL_MESSAGE;
import static com.meawallet.weather.util.WeatherTestUtil.weatherApiDto;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class LatitudeWeatherApiDtoValidationRuleTest {

    @InjectMocks
    LatitudeWeatherApiDtoValidationRule victim;

    @Test
    void validate_whenLatIsNull_thenThrowWeatherApiDtoValidationException() {
        WeatherApiDto request = weatherApiDto();
        request.setLat(null);

        assertThatThrownBy(() -> victim.validate(request))
                .isInstanceOf(WeatherApiDtoValidationException.class)
                .hasMessage(WEATHER_API_LATITUDE_NULL_MESSAGE);
    }

    @Test
    void validate_whenLatNotNull_thenDoNothing() {
        WeatherApiDto request = weatherApiDto();

        assertThatNoException().isThrownBy(() -> victim.validate(request));
    }

}
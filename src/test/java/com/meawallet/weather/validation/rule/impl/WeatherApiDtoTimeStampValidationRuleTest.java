package com.meawallet.weather.validation.rule.impl;

import com.meawallet.weather.handler.exception.WeatherApiDtoValidationException;
import com.meawallet.weather.model.WeatherApiDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.meawallet.weather.message.store.WeatherValidationMessageStore.WEATHER_API_TIMESTAMP_NULL_MESSAGE;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherApiDto;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class WeatherApiDtoTimeStampValidationRuleTest {

    @InjectMocks
    WeatherApiDtoTimeStampValidationRule victim;

    @Test
    void validate_whenTimeStampIsNull_thenThrowWeatherApiDtoValidationException() {
        WeatherApiDto request = weatherApiDto();
        request.setTimeStamp(null);

        assertThatThrownBy(() -> victim.validate(request))
                .isInstanceOf(WeatherApiDtoValidationException.class)
                .hasMessage(WEATHER_API_TIMESTAMP_NULL_MESSAGE);
    }

    @Test
    void validate_whenTimeStampNotNull_thenDoNothing() {
        WeatherApiDto request = weatherApiDto();

        assertThatNoException().isThrownBy(() -> victim.validate(request));
    }

}
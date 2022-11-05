package com.meawallet.weather.business.validation.rule.impl;

import com.meawallet.weather.business.handler.exception.WeatherApiDtoValidationException;
import com.meawallet.weather.business.repository.WeatherRepository;
import com.meawallet.weather.model.WeatherApiDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.meawallet.weather.business.ConstantsStore.WEATHER_API_DTO_EXISTS_MESSAGE;
import static com.meawallet.weather.util.WeatherTestUtil.weatherApiDto;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UcLatLonAltTimeStampWeatherApiDtoValidationRuleTest {

    @Mock
    WeatherRepository repository;
    @InjectMocks
    UcLatLonAltTimeStampWeatherApiDtoValidationRule victim;

    @Test
    void validate_whenFound_thenThrowWeatherApiDtoValidationException() {
        WeatherApiDto request = weatherApiDto();
        when(repository.existsByLatAndLonAndAltitudeAndTimeStamp(
                request.getLat(),
                request.getLon(),
                request.getAltitude(),
                request.getTimeStamp())
        ).thenReturn(true);

        assertThatThrownBy(() -> victim.validate(request))
                .isInstanceOf(WeatherApiDtoValidationException.class)
                .hasMessageContaining(WEATHER_API_DTO_EXISTS_MESSAGE);

        verify(repository, times(1))
                .existsByLatAndLonAndAltitudeAndTimeStamp(
                        request.getLat(),
                        request.getLon(),
                        request.getAltitude(),
                        request.getTimeStamp());

        verifyNoMoreInteractions(repository);
    }

    @Test
    void validate_whenNotFound_thenDoNothing() {
        WeatherApiDto request = weatherApiDto();
        when(repository.existsByLatAndLonAndAltitudeAndTimeStamp(
                request.getLat(),
                request.getLon(),
                request.getAltitude(),
                request.getTimeStamp())
        ).thenReturn(false);

        assertThatNoException().isThrownBy(() -> victim.validate(request));

        verify(repository, times(1)).existsByLatAndLonAndAltitudeAndTimeStamp(
                request.getLat(),
                request.getLon(),
                request.getAltitude(),
                request.getTimeStamp());
        verifyNoMoreInteractions(repository);
    }

}
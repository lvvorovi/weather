package com.meawallet.weather.business.validation.rule.impl;

import com.meawallet.weather.handler.exception.WeatherApiDtoValidationException;
import com.meawallet.weather.business.repository.WeatherRepository;
import com.meawallet.weather.business.validation.rule.WeatherApiDtoValidationRule;
import com.meawallet.weather.model.WeatherApiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.message.store.WeatherValidationMessageStore.buildApiDtoExistsMessage;

@Component
@RequiredArgsConstructor
public class UcLatLonAltTimeStampWeatherApiDtoValidationRule implements WeatherApiDtoValidationRule {

    private final WeatherRepository repository;

    @Override
    public void validate(WeatherApiDto dto) {
        boolean exists = repository
                .existsByLatAndLonAndAltitudeAndTimeStamp(dto.getLat(), dto.getLon(), dto.getAltitude(),
                        dto.getTimeStamp());

        if (exists) {
            throw new WeatherApiDtoValidationException(buildApiDtoExistsMessage(
                    dto.getLat(),
                    dto.getLon(),
                    dto.getAltitude(),
                    dto.getTimeStamp()
            ));
        }
    }
}

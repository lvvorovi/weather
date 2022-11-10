package com.meawallet.weather.service.impl;

import com.meawallet.weather.service.WeatherApiService;
import com.meawallet.weather.service.WeatherService;
import com.meawallet.weather.service.WeatherServiceFacade;
import com.meawallet.weather.util.RequestParamFormatter;
import com.meawallet.weather.handler.exception.WeatherEntityAlreadyExistsException;
import com.meawallet.weather.handler.exception.WeatherEntityNotFoundException;
import com.meawallet.weather.handler.exception.WeatherEntityNotFoundWhenStoredException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.message.store.WeatherServiceFacadeMessageStore.buildFindRequestMessage;
import static com.meawallet.weather.message.store.WeatherServiceFacadeMessageStore.buildNotFoundWhileHasToBeFoundMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceFacadeImpl implements WeatherServiceFacade {

    private final WeatherService service;
    private final WeatherApiService apiService;
    private final RequestParamFormatter requestParamFormatter;

    @Override
    public WeatherResponseDto findByLatAndLonAndAlt(Float lat, Float lon, Integer altitude) {
        log.info(buildFindRequestMessage(lat, lon, altitude));
        lat = requestParamFormatter.formatLatValue(lat);
        lon = requestParamFormatter.formatLonValue(lon);

        try {
            return service.findDtoByLatAndLonAndAlt(lat, lon, altitude);
        } catch (WeatherEntityNotFoundException ex) {
            log.info(ex.getMessage());
        }

        WeatherApiDto apiDto = apiService.getByLatAndLonAndAlt(lat, lon, altitude);
        if (altitude == null) {
            apiDto.setAltitude(null);
        }

        try {
            return service.save(apiDto);
        } catch (WeatherEntityAlreadyExistsException ex) {
            log.info(ex.getMessage());
        }

        try {
            return service.findDtoByLatAndLonAndAlt(lat, lon, altitude);
        } catch (WeatherEntityNotFoundException ex) {
            throw new WeatherEntityNotFoundWhenStoredException(
                    buildNotFoundWhileHasToBeFoundMessage(ex.getMessage(), ex));
        }

    }
}

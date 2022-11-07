package com.meawallet.weather.business.service.impl;

import com.meawallet.weather.handler.exception.WeatherEntityNotFoundException;
import com.meawallet.weather.business.service.WeatherApiService;
import com.meawallet.weather.business.service.WeatherService;
import com.meawallet.weather.business.service.WeatherServiceFacade;
import com.meawallet.weather.business.util.WeatherServiceUtil;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.message.store.WeatherServiceFacadeMessageStore.buildFindRequestMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceFacadeImpl implements WeatherServiceFacade {

    private final WeatherService service;
    private final WeatherApiService apiService;
    private final WeatherServiceUtil util;

    @Override
    public WeatherResponseDto findByLatAndLonAndAlt(Float lat, Float lon, Integer altitude) {
        log.info(buildFindRequestMessage(lat, lon, altitude));
        lat = util.formatLatValue(lat);
        lon = util.formatLonValue(lon);

        try {
            return service.findByLatAndLonAndAlt(lat, lon, altitude);
        } catch (WeatherEntityNotFoundException ex) {
            log.info(ex.getMessage());
        }

        WeatherApiDto apiDto = apiService.getByLatAndLonAndAlt(lat, lon, altitude);

        if (altitude == null) {
            apiDto.setAltitude(null);
        }

        return service.save(apiDto);
    }
}

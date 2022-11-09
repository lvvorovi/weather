package com.meawallet.weather.business.service.impl;

import com.meawallet.weather.business.deserializer.WeatherApiDtoDeserializer;
import com.meawallet.weather.business.service.WeatherApiService;
import com.meawallet.weather.business.util.YrWeatherApiServiceUtil;
import com.meawallet.weather.handler.exception.WeatherApiServiceException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.properties.WeatherProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.meawallet.weather.business.message.store.WeatherApiServiceMessageStore.buildApiCallExceptionMessage;
import static com.meawallet.weather.business.message.store.WeatherApiServiceMessageStore.buildApiCallMessage;
import static com.meawallet.weather.business.message.store.WeatherApiServiceMessageStore.buildApiResponseMessage;

@Service
@Slf4j
@RequiredArgsConstructor
public class YrWeatherApiService implements WeatherApiService {

    private final RestTemplate restTemplate;
    private final WeatherApiDtoDeserializer deserializer;
    private final YrWeatherApiServiceUtil yrApiUtil;
    private final WeatherProperties properties;

    @Override
    public WeatherApiDto getByLatAndLonAndAlt(Float lat, Float lon, Integer alt) {
        ResponseEntity<String> responseEntity;
        Map<String, String> params = yrApiUtil.buildUrlParams(lat, lon, alt);
        String urlTemplate = yrApiUtil.buildRequestUrl(properties.getApiUrlCompact(), params);
        HttpHeaders headers = yrApiUtil.getRequiredHeaders(properties.getUserAgentHeaderValue());

        if (log.isDebugEnabled()) {
            log.debug(buildApiCallMessage(urlTemplate));
        }

        try {
            responseEntity = restTemplate.exchange(
                    urlTemplate,
                    HttpMethod.GET,
                    new HttpEntity<>(null, headers),
                    String.class);
        } catch (RestClientException ex) {
            throw new WeatherApiServiceException(buildApiCallExceptionMessage(ex.getMessage()), ex);
        }

        if (log.isDebugEnabled()) {
            log.debug(buildApiResponseMessage(responseEntity.getBody()));
        }

        yrApiUtil.validateBody(responseEntity);
        yrApiUtil.validateResponseStatus(responseEntity);
        return deserializer.deserializeApiResponse(responseEntity.getBody());
    }

}

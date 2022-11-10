package com.meawallet.weather.service.impl;

import com.meawallet.weather.deserializer.WeatherApiDtoDeserializer;
import com.meawallet.weather.handler.exception.WeatherApiServiceException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.payload.YrApiServiceRequestDto;
import com.meawallet.weather.service.WeatherApiService;
import com.meawallet.weather.util.YrServiceRequestBuilder;
import com.meawallet.weather.validation.service.impl.YrResponseValidationServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiCallExceptionMessage;
import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiCallMessage;
import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiResponseMessage;
import static org.springframework.http.HttpMethod.GET;

@Service
@Slf4j
@RequiredArgsConstructor
public class YrWeatherApiService implements WeatherApiService {

    private final RestTemplate restTemplate;
    private final WeatherApiDtoDeserializer deserializer;
    private final YrResponseValidationServiceImpl responseValidator;
    private final YrServiceRequestBuilder requestBuilder;

    @Override
    public WeatherApiDto getByLatAndLonAndAlt(Float lat, Float lon, Integer alt) {
        YrApiServiceRequestDto requestDto = requestBuilder.build(lat, lon, alt, GET);

        if (log.isDebugEnabled()) {
            log.debug(buildApiCallMessage(requestDto.getUrl()));
        }

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(
                    requestDto.getUrl(),
                    requestDto.getHttpMethod(),
                    requestDto.getHttpEntity(),
                    String.class);
        } catch (RestClientException ex) {
            throw new WeatherApiServiceException(buildApiCallExceptionMessage(ex.getMessage()), ex);
        }

        if (log.isDebugEnabled()) {
            log.debug(buildApiResponseMessage(responseEntity.getBody()));
        }

        responseValidator.validateStringResponse(responseEntity);
        return deserializer.deserializeApiResponse(responseEntity.getBody());
    }

}

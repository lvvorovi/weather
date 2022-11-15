package com.meawallet.weather.service.impl;

import com.meawallet.weather.handler.exception.YrApiCallExecutorException;
import com.meawallet.weather.payload.YrApiServiceRequestDto;
import com.meawallet.weather.service.WeatherApiCallExecutor;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.meawallet.weather.message.store.WeatherApiCallExecutorMessageStore.buildApiCallExceptionMessage;

@Component
@RequiredArgsConstructor
public class YrApiCallExecutor implements WeatherApiCallExecutor {

    private final RestTemplate restTemplate;

    @Override
    @Timed(value = "YrApiCallExecutor")
    public ResponseEntity<String> execute(YrApiServiceRequestDto requestDto, Class<String> stringClass) {
        try {
            return restTemplate.exchange(
                    requestDto.getUrl(),
                    requestDto.getHttpMethod(),
                    requestDto.getHttpEntity(),
                    String.class);
        } catch (RestClientException ex) {
            throw new YrApiCallExecutorException(buildApiCallExceptionMessage(ex.getMessage()), ex);
        }
    }
}

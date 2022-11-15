package com.meawallet.weather.validation.rule.impl;

import com.meawallet.weather.handler.exception.WeatherApiServiceException;
import com.meawallet.weather.validation.rule.YrStringResponseValidationRule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiNoResponseMessage;

@Component
public class YrResponseBodyValidationRule implements YrStringResponseValidationRule {

    @Override
    public void validateStringResponse(ResponseEntity<String> responseEntity) {
        String body = responseEntity.getBody();

        if (body == null || body.isBlank()) {
            throw new WeatherApiServiceException(buildApiNoResponseMessage(responseEntity.getStatusCode()));
        }
    }
}

package com.meawallet.weather.validation.rule.impl;

import com.meawallet.weather.handler.exception.WeatherApiServiceException;
import com.meawallet.weather.validation.rule.YrStringResponseValidationRule;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiInvalidRequestMessage;

@Component
public class YrResponseStatusValidationRule implements YrStringResponseValidationRule {

    @Override
    public void validateStringResponse(ResponseEntity<String> responseEntity) {
        boolean isNot200 = !responseEntity.getStatusCode().is2xxSuccessful();

        if (isNot200) {
            throw new WeatherApiServiceException(buildApiInvalidRequestMessage(responseEntity.getBody()));
        }
    }
}

package com.meawallet.weather.validation.rule.impl;

import com.meawallet.weather.handler.exception.WeatherApiServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiInvalidRequestMessage;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class YrResponseStatusValidationRuleTest {

    @InjectMocks
    YrResponseStatusValidationRule victim;

    @Test
    void validateStringResponse_whenIs200_theDoNothing() {
        ResponseEntity<String> responseEntity = ResponseEntity.ok().build();

        assertThatNoException().isThrownBy(() -> victim.validateStringResponse(responseEntity));
    }

    @Test
    void validateStringResponse_whenIsNot200_theThrowWeatherApiServiceException() {
        ResponseEntity<String> responseEntity = ResponseEntity.badRequest().build();

        assertThatThrownBy(() -> victim.validateStringResponse(responseEntity))
                .isInstanceOf(WeatherApiServiceException.class)
                .hasMessage(buildApiInvalidRequestMessage(responseEntity.getBody()));
    }

}
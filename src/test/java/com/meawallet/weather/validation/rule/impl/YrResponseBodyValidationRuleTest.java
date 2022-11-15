package com.meawallet.weather.validation.rule.impl;

import com.meawallet.weather.handler.exception.WeatherApiServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiNoResponseMessage;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class YrResponseBodyValidationRuleTest {

    @InjectMocks
    YrResponseBodyValidationRule victim;

    @Test
    void validateStringResponse_whenBodyNotEmpty_thenDoNothing() {
        ResponseEntity<String> responseEntity = ResponseEntity.ok("TestResponseBody");

        assertThatNoException().isThrownBy(() -> victim.validateStringResponse(responseEntity));
    }

    @Test
    void validateStringResponse_whenBodyIsBlank_thenThrowWeatherApiServiceException() {
        ResponseEntity<String> responseEntity = ResponseEntity.ok("");

        assertThatThrownBy(() -> victim.validateStringResponse(responseEntity))
                .isInstanceOf(WeatherApiServiceException.class)
                .hasMessage(buildApiNoResponseMessage(responseEntity.getStatusCode()));
    }

    @Test
    void validateStringResponse_whenBodyIsNull_thenThrowWeatherApiServiceException() {
        ResponseEntity<String> responseEntity = ResponseEntity.ok(null);

        assertThatThrownBy(() -> victim.validateStringResponse(responseEntity))
                .isInstanceOf(WeatherApiServiceException.class)
                .hasMessage(buildApiNoResponseMessage(responseEntity.getStatusCode()));
    }

}
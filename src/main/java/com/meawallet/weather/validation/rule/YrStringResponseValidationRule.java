package com.meawallet.weather.validation.rule;

import org.springframework.http.ResponseEntity;

public interface YrStringResponseValidationRule {

    void validateStringResponse(ResponseEntity<String> responseEntity);
}

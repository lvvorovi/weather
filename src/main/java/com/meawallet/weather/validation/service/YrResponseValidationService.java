package com.meawallet.weather.validation.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public interface YrResponseValidationService {
    void validateStringResponse(ResponseEntity<String> responseEntity);
}

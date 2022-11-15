package com.meawallet.weather.validation.service.impl;

import com.meawallet.weather.validation.rule.YrStringResponseValidationRule;
import com.meawallet.weather.validation.service.YrResponseValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class YrResponseValidationServiceImpl implements YrResponseValidationService {

    private final List<YrStringResponseValidationRule> stringValidationRuleList;

    @Override
    public void validateStringResponse(ResponseEntity<String> responseEntity) {
        stringValidationRuleList.forEach(rule -> rule.validateStringResponse(responseEntity));
    }

}

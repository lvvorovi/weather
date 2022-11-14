package com.meawallet.weather.validation.service.impl;

import com.meawallet.weather.validation.rule.YrStringResponseValidationRule;
import com.meawallet.weather.validation.rule.impl.YrResponseBodyValidationRule;
import com.meawallet.weather.validation.rule.impl.YrResponseStatusValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class YrResponseValidationServiceImplTest {

    private List<YrStringResponseValidationRule> ruleList;

    @Mock
    YrResponseStatusValidationRule statusValidationRule;
    @Mock
    YrResponseBodyValidationRule bodyValidationRule;

    @InjectMocks
    YrResponseValidationServiceImpl victim;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(statusValidationRule);
        ruleList.add(bodyValidationRule);
        victim = new YrResponseValidationServiceImpl(ruleList);
    }

    @Test
    void validateStringResponse_whenCalled_delegatesToEachRule() {
        ResponseEntity<String> responseEntity = ResponseEntity.ok().build();
        ruleList.forEach(rule -> doNothing().when(rule).validateStringResponse(responseEntity));

        victim.validateStringResponse(responseEntity);

        ruleList.forEach(rule ->
                verify(rule, times(1)).validateStringResponse(responseEntity));
        ruleList.forEach(Mockito::verifyNoMoreInteractions);
    }

    @Test
    void validateStringResponse_WhenNoRulesInjected_thenNoRulesUsed() {
        ResponseEntity<String> responseEntity = ResponseEntity.ok().build();
        victim = new YrResponseValidationServiceImpl(new ArrayList<>());

        victim.validateStringResponse(responseEntity);

        ruleList.forEach(Mockito::verifyNoInteractions);
    }

}
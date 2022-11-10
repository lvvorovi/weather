package com.meawallet.weather.validation.service.impl;

import com.meawallet.weather.validation.rule.WeatherApiDtoValidationRule;
import com.meawallet.weather.validation.service.WeatherValidationService;
import com.meawallet.weather.model.WeatherApiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WeatherValidationServiceImpl implements WeatherValidationService {

    private final List<WeatherApiDtoValidationRule> apiDtoRuleList;

    @Override
    public void validate(WeatherApiDto weatherApiDto) {
        apiDtoRuleList.forEach(rule -> rule.validate(weatherApiDto));
    }
}

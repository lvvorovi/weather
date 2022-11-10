package com.meawallet.weather.validation.service.impl;

import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.validation.rule.WeatherApiDtoValidationRule;
import com.meawallet.weather.validation.rule.impl.WeatherApiDtoLatitudeValidationRule;
import com.meawallet.weather.validation.rule.impl.WeatherApiDtoLongitudeValidationRule;
import com.meawallet.weather.validation.rule.impl.WeatherApiDtoTemperatureValidationRule;
import com.meawallet.weather.validation.rule.impl.WeatherApiDtoTimeStampValidationRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static com.meawallet.weather.util.WeatherTestUtil.weatherApiDto;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class WeatherValidationServiceImplTest {

    @Mock
    WeatherApiDtoLatitudeValidationRule latitudeRule;
    @Mock
    WeatherApiDtoLongitudeValidationRule longitudeRule;
    @Mock
    WeatherApiDtoTemperatureValidationRule temperatureRule;
    @Mock
    WeatherApiDtoTimeStampValidationRule timeStampRule;

    List<WeatherApiDtoValidationRule> ruleList;
    WeatherValidationServiceImpl victim;

    @BeforeEach
    void setUp() {
        ruleList = new ArrayList<>();
        ruleList.add(latitudeRule);
        ruleList.add(longitudeRule);
        ruleList.add(temperatureRule);
        ruleList.add(timeStampRule);
        victim = new WeatherValidationServiceImpl(ruleList);
    }

    @Test
    void validate_whenValidateWeatherApiDto_thenDelegateToEachRule() {
        WeatherApiDto mockDto = mock(WeatherApiDto.class);
        ruleList.forEach(rule -> doNothing().when(rule).validate(mockDto));

        victim.validate(mockDto);

        ruleList.forEach(rule ->
                verify(rule, times(1)).validate(mockDto));
        ruleList.forEach(Mockito::verifyNoMoreInteractions);
    }

    @Test
    void validate_whenNoRulesInjected_thenNoValidationRuleUsed() {
        victim = new WeatherValidationServiceImpl(List.of());

        victim.validate(weatherApiDto());

        ruleList.forEach(Mockito::verifyNoInteractions);
    }

}
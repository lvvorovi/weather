package com.meawallet.weather.service.impl;

import com.meawallet.weather.deserializer.WeatherApiDtoDeserializer;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.payload.YrApiServiceRequestDto;
import com.meawallet.weather.service.WeatherApiCallExecutor;
import com.meawallet.weather.util.YrServiceRequestBuilder;
import com.meawallet.weather.validation.service.impl.YrResponseValidationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static com.meawallet.weather.test.util.WeatherTestUtil.ALTITUDE;
import static com.meawallet.weather.test.util.WeatherTestUtil.LAT;
import static com.meawallet.weather.test.util.WeatherTestUtil.LON;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherApiDto;
import static com.meawallet.weather.test.util.WeatherTestUtil.yrApiServiceRequestDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith({MockitoExtension.class})
class YrWeatherApiServiceTest {

    @Mock
    WeatherApiCallExecutor apiCallExecutor;
    @Mock
    WeatherApiDtoDeserializer deserializer;
    @Mock
    YrResponseValidationServiceImpl responseValidator;
    @Mock
    YrServiceRequestBuilder requestBuilder;

    @InjectMocks
    YrWeatherApiService victim;

    @Test
    void getByLatAndLonAndAlt_whenAllParams_thenValidResponse() {
        WeatherApiDto expected = weatherApiDto();
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("Body");
        YrApiServiceRequestDto requestDto = yrApiServiceRequestDto();
        when(requestBuilder.build(LAT, LON, ALTITUDE, GET)).thenReturn(requestDto);
        when(apiCallExecutor.execute(requestDto, String.class))
                .thenReturn(responseEntity);

        doNothing().when(responseValidator).validateStringResponse(responseEntity);
        when(deserializer.deserializeApiResponse(responseEntity.getBody())).thenReturn(expected);

        WeatherApiDto result = victim.getByLatAndLonAndAlt(LAT, LON, ALTITUDE);

        assertEquals(expected, result);
        verify(requestBuilder, times(1)).build(LAT, LON, ALTITUDE, GET);
        verify(apiCallExecutor, times(1)).execute(requestDto, String.class);
        verify(responseValidator, times(1)).validateStringResponse(responseEntity);
        verify(deserializer, times(1)).deserializeApiResponse(responseEntity.getBody());
        verifyNoMoreInteractions(requestBuilder, apiCallExecutor, responseValidator, deserializer);
    }
}
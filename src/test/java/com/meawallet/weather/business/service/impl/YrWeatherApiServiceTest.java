package com.meawallet.weather.business.service.impl;

import com.meawallet.weather.business.deserializer.WeatherApiDtoDeserializer;
import com.meawallet.weather.business.util.YrWeatherApiServiceUtil;
import com.meawallet.weather.model.WeatherApiDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import static com.meawallet.weather.business.ConstantsStore.WEATHER_API_CALL_LOG;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_API_RESPONSE_LOG;
import static com.meawallet.weather.util.WeatherTestUtil.ALTITUDE;
import static com.meawallet.weather.util.WeatherTestUtil.LAT;
import static com.meawallet.weather.util.WeatherTestUtil.LON;
import static com.meawallet.weather.util.WeatherTestUtil.WEATHER_API_URL_WITH_ALL_PARAMS;
import static com.meawallet.weather.util.WeatherTestUtil.WEATHER_API_URL_WITH_NO_ALT_PARAM;
import static com.meawallet.weather.util.WeatherTestUtil.getRequiredHeaders;
import static com.meawallet.weather.util.WeatherTestUtil.getUrlParamsNoALt;
import static com.meawallet.weather.util.WeatherTestUtil.getUrlParamsWithAlt;
import static com.meawallet.weather.util.WeatherTestUtil.weatherApiDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
@ActiveProfiles("test.properties")
class YrWeatherApiServiceTest {

    @Mock
    YrWeatherApiServiceUtil util;
    @Mock
    RestTemplate restTemplate;
    @Mock
    WeatherApiDtoDeserializer deserializer;

    @InjectMocks
    YrWeatherApiService victim;

    @Test
    void getByLatAndLonAndAlt_whenAllParams_thenValidUrl(CapturedOutput output) {
        WeatherApiDto expected = weatherApiDto();
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("Body");
        when(util.buildUrlParams(LAT, LON, ALTITUDE)).thenReturn(getUrlParamsWithAlt());
        when(util.buildRequestUrl(null, getUrlParamsWithAlt())).thenReturn(WEATHER_API_URL_WITH_ALL_PARAMS);
        when(util.getRequiredHeaders()).thenReturn(getRequiredHeaders());

        when(restTemplate.exchange(
                WEATHER_API_URL_WITH_ALL_PARAMS,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class)
        ).thenReturn(responseEntity);

        doNothing().when(util).validateBody(responseEntity);
        doNothing().when(util).validateResponseStatus(responseEntity);
        when(deserializer.deserializeApiResponse(responseEntity.getBody())).thenReturn(expected);

        WeatherApiDto result = victim.getByLatAndLonAndAlt(LAT, LON, ALTITUDE);

        assertEquals(expected, result);
        assertThat(output.getOut()).contains(WEATHER_API_CALL_LOG + WEATHER_API_URL_WITH_ALL_PARAMS);
        assertThat(output.getOut()).contains(WEATHER_API_RESPONSE_LOG + responseEntity.getBody());
        verify(util, times(1)).buildUrlParams(LAT, LON, ALTITUDE);
        verify(util, times(1)).buildRequestUrl(null, getUrlParamsWithAlt());
        verify(util, times(1)).getRequiredHeaders();

        verify(restTemplate, times(1)).exchange(
                WEATHER_API_URL_WITH_ALL_PARAMS,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class);

        verify(util, times(1)).validateBody(responseEntity);
        verify(util, times(1)).validateResponseStatus(responseEntity);
        verify(deserializer, times(1)).deserializeApiResponse(responseEntity.getBody());
        verifyNoMoreInteractions(util, deserializer, restTemplate);
    }


    @Test
    void getByLatAndLonAndAlt_whenNoAltParam_thenValidUrl(CapturedOutput output) {
        WeatherApiDto expected = weatherApiDto();
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("Body");
        when(util.buildUrlParams(LAT, LON, null)).thenReturn(getUrlParamsNoALt());
        when(util.buildRequestUrl(null, getUrlParamsNoALt())).thenReturn(WEATHER_API_URL_WITH_NO_ALT_PARAM);
        when(util.getRequiredHeaders()).thenReturn(getRequiredHeaders());

        when(restTemplate.exchange(
                WEATHER_API_URL_WITH_NO_ALT_PARAM,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class)
        ).thenReturn(responseEntity);

        doNothing().when(util).validateBody(responseEntity);
        doNothing().when(util).validateResponseStatus(responseEntity);
        when(deserializer.deserializeApiResponse(responseEntity.getBody())).thenReturn(expected);

        WeatherApiDto result = victim.getByLatAndLonAndAlt(LAT, LON, null);

        assertEquals(expected, result);
        assertThat(output.getOut()).contains(WEATHER_API_CALL_LOG + WEATHER_API_URL_WITH_NO_ALT_PARAM);
        assertThat(output.getOut()).contains(WEATHER_API_RESPONSE_LOG + responseEntity.getBody());
        verify(util, times(1)).buildUrlParams(LAT, LON, null);
        verify(util, times(1)).buildRequestUrl(null, getUrlParamsNoALt());
        verify(util, times(1)).getRequiredHeaders();

        verify(restTemplate, times(1)).exchange(
                WEATHER_API_URL_WITH_NO_ALT_PARAM,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class);

        verify(util, times(1)).validateBody(responseEntity);
        verify(util, times(1)).validateResponseStatus(responseEntity);
        verify(deserializer, times(1)).deserializeApiResponse(responseEntity.getBody());
        verifyNoMoreInteractions(util, deserializer, restTemplate);
    }

}
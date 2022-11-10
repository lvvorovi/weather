package com.meawallet.weather.util;

import com.meawallet.weather.payload.YrApiServiceRequestDto;
import com.meawallet.weather.properties.WeatherProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;

import static com.meawallet.weather.test.util.WeatherTestUtil.ALTITUDE;
import static com.meawallet.weather.test.util.WeatherTestUtil.LAT;
import static com.meawallet.weather.test.util.WeatherTestUtil.LON;
import static com.meawallet.weather.test.util.WeatherTestUtil.TEST_USER_AGENT_VALUE;
import static com.meawallet.weather.test.util.WeatherTestUtil.WEATHER_API_URL_COMPACT;
import static com.meawallet.weather.test.util.WeatherTestUtil.WEATHER_API_URL_NO_ALT_PARAM;
import static com.meawallet.weather.test.util.WeatherTestUtil.WEATHER_API_URL_WITH_ALL_PARAMS;
import static com.meawallet.weather.test.util.WeatherTestUtil.WEATHER_API_URL_WITH_NO_ALT_PARAM;
import static com.meawallet.weather.test.util.WeatherTestUtil.getRequiredHeaders;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith(MockitoExtension.class)
class YrServiceRequestBuilderTest {

    @Mock
    WeatherProperties properties;

    @InjectMocks
    YrServiceRequestBuilder victim;

    @Test
    void build_whenAllParametersProvided_thenReturnRequestDto() {
        YrApiServiceRequestDto expected = YrApiServiceRequestDto.builder()
                .url(WEATHER_API_URL_WITH_ALL_PARAMS)
                .httpMethod(GET)
                .httpEntity(new HttpEntity<>(null, getRequiredHeaders()))
                .build();
        when(properties.getApiUrlCompact()).thenReturn(WEATHER_API_URL_COMPACT);
        when(properties.getUserAgentHeaderValue()).thenReturn(TEST_USER_AGENT_VALUE);

        var result = victim.build(LAT, LON, ALTITUDE, GET);

        assertEquals(expected, result);
    }
    @Test
    void build_whenNoAltParamProvided_thenReturnRequestDto() {
        YrApiServiceRequestDto expected = YrApiServiceRequestDto.builder()
                .url(WEATHER_API_URL_WITH_NO_ALT_PARAM)
                .httpMethod(GET)
                .httpEntity(new HttpEntity<>(null, getRequiredHeaders()))
                .build();
        when(properties.getApiUrlCompact()).thenReturn(WEATHER_API_URL_COMPACT);
        when(properties.getUserAgentHeaderValue()).thenReturn(TEST_USER_AGENT_VALUE);

        var result = victim.build(LAT, LON, null, GET);

        assertEquals(expected, result);
    }

}
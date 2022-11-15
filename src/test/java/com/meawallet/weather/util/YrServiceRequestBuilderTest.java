package com.meawallet.weather.util;

import com.meawallet.weather.payload.YrApiServiceRequestDto;
import com.meawallet.weather.properties.WeatherProperties;
import com.meawallet.weather.test.util.WeatherTestUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;

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
                .url(WeatherTestUtil.WEATHER_API_URL_WITH_ALL_PARAMS)
                .httpMethod(GET)
                .httpEntity(new HttpEntity<>(null, WeatherTestUtil.getRequiredHeaders()))
                .build();
        when(properties.getApiUrlCompact()).thenReturn(WeatherTestUtil.WEATHER_API_URL_COMPACT);
        when(properties.getUserAgentHeaderValue()).thenReturn(WeatherTestUtil.TEST_USER_AGENT_VALUE);

        var result = victim.build(WeatherTestUtil.LAT, WeatherTestUtil.LON, WeatherTestUtil.ALTITUDE, GET);

        Assertions.assertEquals(expected, result);
    }

    @Test
    void build_whenNoAltParamProvided_thenReturnRequestDto() {
        YrApiServiceRequestDto expected = YrApiServiceRequestDto.builder()
                .url(WeatherTestUtil.WEATHER_API_URL_WITH_NO_ALT_PARAM)
                .httpMethod(GET)
                .httpEntity(new HttpEntity<>(null, WeatherTestUtil.getRequiredHeaders()))
                .build();
        when(properties.getApiUrlCompact()).thenReturn(WeatherTestUtil.WEATHER_API_URL_COMPACT);
        when(properties.getUserAgentHeaderValue()).thenReturn(WeatherTestUtil.TEST_USER_AGENT_VALUE);

        var result = victim.build(WeatherTestUtil.LAT, WeatherTestUtil.LON, null, GET);

        Assertions.assertEquals(expected, result);
    }

}
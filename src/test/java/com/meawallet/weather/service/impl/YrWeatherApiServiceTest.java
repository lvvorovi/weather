package com.meawallet.weather.service.impl;

import com.meawallet.weather.deserializer.WeatherApiDtoDeserializer;
import com.meawallet.weather.handler.exception.WeatherApiServiceException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.payload.YrApiServiceRequestDto;
import com.meawallet.weather.util.YrServiceRequestBuilder;
import com.meawallet.weather.validation.service.impl.YrResponseValidationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiCallExceptionMessage;
import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiCallMessage;
import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.buildApiResponseMessage;
import static com.meawallet.weather.test.util.WeatherTestUtil.ALTITUDE;
import static com.meawallet.weather.test.util.WeatherTestUtil.LAT;
import static com.meawallet.weather.test.util.WeatherTestUtil.LON;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherApiDto;
import static com.meawallet.weather.test.util.WeatherTestUtil.yrApiServiceRequestDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
@ActiveProfiles("test.properties")
class YrWeatherApiServiceTest {

    @Mock
    RestTemplate restTemplate;
    @Mock
    WeatherApiDtoDeserializer deserializer;
    @Mock
    YrResponseValidationServiceImpl responseValidator;
    @Mock
    YrServiceRequestBuilder requestBuilder;

    @InjectMocks
    YrWeatherApiService victim;

    /*@Test
    void getByLatAndLonAndAlt_whenAllParams_thenValidUrl(CapturedOutput output) {
        WeatherApiDto expected = weatherApiDto();
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("Body");
        when(responseValidator.buildUrlParams(LAT, LON, ALTITUDE)).thenReturn(getUrlParamsWithAlt());
        when(properties.getApiUrlCompact()).thenReturn(WEATHER_API_URL);
        when(responseValidator.buildRequestUrl(WEATHER_API_URL, getUrlParamsWithAlt())).thenReturn
        (WEATHER_API_URL_WITH_ALL_PARAMS);
        when(properties.getUserAgentHeaderValue()).thenReturn(USER_AGENT);
        when(responseValidator.getRequiredHeaders(USER_AGENT)).thenReturn(getRequiredHeaders());

        when(restTemplate.exchange(
                WEATHER_API_URL_WITH_ALL_PARAMS,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class)
        ).thenReturn(responseEntity);

        doNothing().when(responseValidator).validateBody(responseEntity);
        doNothing().when(responseValidator).validateResponseStatus(responseEntity);
        when(deserializer.deserializeApiResponse(responseEntity.getBody())).thenReturn(expected);

        WeatherApiDto result = victim.getByLatAndLonAndAlt(LAT, LON, ALTITUDE);

        assertEquals(expected, result);
        assertThat(output.getOut()).contains(buildApiCallMessage(WEATHER_API_URL_WITH_ALL_PARAMS));
        assertThat(output.getOut()).contains(buildApiResponseMessage(responseEntity.getBody()));
        verify(responseValidator, times(1)).buildUrlParams(LAT, LON, ALTITUDE);
        verify(properties, times(1)).getApiUrlCompact();
        verify(responseValidator, times(1)).buildRequestUrl(WEATHER_API_URL, getUrlParamsWithAlt());
        verify(responseValidator, times(1)).getRequiredHeaders(USER_AGENT);

        verify(restTemplate, times(1)).exchange(
                WEATHER_API_URL_WITH_ALL_PARAMS,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class);

        verify(responseValidator, times(1)).validateBody(responseEntity);
        verify(responseValidator, times(1)).validateResponseStatus(responseEntity);
        verify(deserializer, times(1)).deserializeApiResponse(responseEntity.getBody());
        verifyNoMoreInteractions(responseValidator, deserializer, restTemplate, properties);
    }*/

    @Test
    void getByLatAndLonAndAlt_whenAllParams_thenValidResponse(CapturedOutput output) {
        WeatherApiDto expected = weatherApiDto();
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("Body");
        YrApiServiceRequestDto requestDto = yrApiServiceRequestDto();
        when(requestBuilder.build(LAT, LON, ALTITUDE, GET)).thenReturn(requestDto);
        when(restTemplate.exchange(
                requestDto.getUrl(),
                requestDto.getHttpMethod(),
                requestDto.getHttpEntity(),
                String.class))
                .thenReturn(responseEntity);

        doNothing().when(responseValidator).validateStringResponse(responseEntity);
        when(deserializer.deserializeApiResponse(responseEntity.getBody())).thenReturn(expected);

        WeatherApiDto result = victim.getByLatAndLonAndAlt(LAT, LON, ALTITUDE);

        assertEquals(expected, result);
        assertThat(output.getOut()).contains(buildApiCallMessage(requestDto.getUrl()));
        assertThat(output.getOut()).contains(buildApiResponseMessage(responseEntity.getBody()));
        verify(requestBuilder, times(1)).build(LAT, LON, ALTITUDE, GET);
        verify(restTemplate, times(1)).exchange(
                requestDto.getUrl(),
                requestDto.getHttpMethod(),
                requestDto.getHttpEntity(),
                String.class);
        verify(responseValidator, times(1)).validateStringResponse(responseEntity);
        verify(deserializer, times(1)).deserializeApiResponse(responseEntity.getBody());
        verifyNoMoreInteractions(requestBuilder, restTemplate, responseValidator, deserializer);
    }

    @Test
    void getByLatAndLonAndAlt_whenRestTemplateThrowsRestClientException_thenThrowWeatherApiServiceException(CapturedOutput output) {
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("Body");
        YrApiServiceRequestDto requestDto = yrApiServiceRequestDto();
        when(requestBuilder.build(LAT, LON, ALTITUDE, GET)).thenReturn(requestDto);
        when(restTemplate.exchange(
                requestDto.getUrl(),
                requestDto.getHttpMethod(),
                requestDto.getHttpEntity(),
                String.class))
                .thenThrow(new RestClientException("TestRestClientExceptionMessage"));

        assertThatThrownBy(() -> victim.getByLatAndLonAndAlt(LAT, LON, ALTITUDE))
                .isInstanceOf(WeatherApiServiceException.class)
                .hasMessage(buildApiCallExceptionMessage("TestRestClientExceptionMessage"));

        assertThat(output.getOut()).contains(buildApiCallMessage(requestDto.getUrl()));
        assertThat(output.getOut()).doesNotContain(buildApiResponseMessage(responseEntity.getBody()));
        verify(requestBuilder, times(1)).build(LAT, LON, ALTITUDE, GET);
        verify(restTemplate, times(1)).exchange(
                requestDto.getUrl(),
                requestDto.getHttpMethod(),
                requestDto.getHttpEntity(),
                String.class);

        verifyNoMoreInteractions(requestBuilder, restTemplate);
        verifyNoInteractions(responseValidator, deserializer);
    }


   /* @Test
    void getByLatAndLonAndAlt_whenNoAltParam_thenValidUrl(CapturedOutput output) {
        WeatherApiDto expected = weatherApiDto();
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("Body");
        when(responseValidator.buildUrlParams(LAT, LON, null)).thenReturn(getUrlParamsNoALt());
        when(properties.getApiUrlCompact()).thenReturn(WEATHER_API_URL);
        when(responseValidator.buildRequestUrl(WEATHER_API_URL, getUrlParamsNoALt())).thenReturn
        (WEATHER_API_URL_WITH_NO_ALT_PARAM);
        when(properties.getUserAgentHeaderValue()).thenReturn(USER_AGENT);
        when(responseValidator.getRequiredHeaders(USER_AGENT)).thenReturn(getRequiredHeaders());

        when(restTemplate.exchange(
                WEATHER_API_URL_WITH_NO_ALT_PARAM,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class)
        ).thenReturn(responseEntity);

        doNothing().when(responseValidator).validateBody(responseEntity);
        doNothing().when(responseValidator).validateResponseStatus(responseEntity);
        when(deserializer.deserializeApiResponse(responseEntity.getBody())).thenReturn(expected);

        WeatherApiDto result = victim.getByLatAndLonAndAlt(LAT, LON, null);

        assertEquals(expected, result);
        assertThat(output.getOut()).contains(buildApiCallMessage(WEATHER_API_URL_WITH_NO_ALT_PARAM));
        assertThat(output.getOut()).contains(buildApiResponseMessage(responseEntity.getBody()));
        verify(responseValidator, times(1)).buildUrlParams(LAT, LON, null);
        verify(properties, times(1)).getApiUrlCompact();
        verify(responseValidator, times(1)).buildRequestUrl(WEATHER_API_URL, getUrlParamsNoALt());
        verify(responseValidator, times(1)).getRequiredHeaders(USER_AGENT);

        verify(restTemplate, times(1)).exchange(
                WEATHER_API_URL_WITH_NO_ALT_PARAM,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class);

        verify(responseValidator, times(1)).validateBody(responseEntity);
        verify(responseValidator, times(1)).validateResponseStatus(responseEntity);
        verify(deserializer, times(1)).deserializeApiResponse(responseEntity.getBody());
        verifyNoMoreInteractions(responseValidator, deserializer, restTemplate, properties);
    }

    @Test
    void getByLatAndLonAndAlt_whenRestTemplateThrowsException_thenThrowWeatherApiServiceException(CapturedOutput
    output) {
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body("Body");
        when(responseValidator.buildUrlParams(LAT, LON, null)).thenReturn(getUrlParamsNoALt());
        when(properties.getApiUrlCompact()).thenReturn(WEATHER_API_URL);
        when(responseValidator.buildRequestUrl(WEATHER_API_URL, getUrlParamsNoALt())).thenReturn
        (WEATHER_API_URL_WITH_NO_ALT_PARAM);
        when(properties.getUserAgentHeaderValue()).thenReturn(USER_AGENT);
        when(responseValidator.getRequiredHeaders(USER_AGENT)).thenReturn(getRequiredHeaders());

        when(restTemplate.exchange(
                WEATHER_API_URL_WITH_NO_ALT_PARAM,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class)
        ).thenThrow(new RestClientException("Exception message"));

        assertThatThrownBy(() -> victim.getByLatAndLonAndAlt(LAT, LON, null))
                .isInstanceOf(WeatherApiServiceException.class)
                .hasMessage(buildApiCallExceptionMessage("Exception message"));

        assertThat(output.getOut()).contains(buildApiCallMessage(WEATHER_API_URL_WITH_NO_ALT_PARAM));
        assertThat(output.getOut()).doesNotContain(buildApiResponseMessage(responseEntity.getBody()));
        verify(responseValidator, times(1)).buildUrlParams(LAT, LON, null);
        verify(properties, times(1)).getApiUrlCompact();
        verify(responseValidator, times(1)).buildRequestUrl(WEATHER_API_URL, getUrlParamsNoALt());
        verify(responseValidator, times(1)).getRequiredHeaders(USER_AGENT);

        verify(restTemplate, times(1)).exchange(
                WEATHER_API_URL_WITH_NO_ALT_PARAM,
                HttpMethod.GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class);

        verifyNoMoreInteractions(responseValidator, deserializer, restTemplate, properties);
    }*/

}
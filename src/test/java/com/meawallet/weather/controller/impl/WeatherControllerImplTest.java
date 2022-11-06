package com.meawallet.weather.controller.impl;

import com.meawallet.weather.business.service.WeatherServiceFacade;
import com.meawallet.weather.model.ErrorDto;
import com.meawallet.weather.model.WeatherResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.meawallet.weather.util.JsonTestUtil.jsonToErrorDto;
import static com.meawallet.weather.util.JsonTestUtil.jsonToWeatherResponseDto;
import static com.meawallet.weather.util.WeatherTestUtil.ALTITUDE;
import static com.meawallet.weather.util.WeatherTestUtil.WEATHER_CONTROLLER_FIND_URL_WITH_PARAMS;
import static com.meawallet.weather.util.WeatherTestUtil.WEATHER_CONTROLLER_FIND_URL_MISSING_REQUIRED_PARAMS;
import static com.meawallet.weather.util.WeatherTestUtil.WEATHER_CONTROLLER_FIND_URL_WRONG_TYPE_PARAMS;
import static com.meawallet.weather.util.WeatherTestUtil.LAT;
import static com.meawallet.weather.util.WeatherTestUtil.LON;
import static com.meawallet.weather.util.WeatherTestUtil.weatherResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WeatherControllerImpl.class)
class WeatherControllerImplTest {

    @MockBean
    WeatherServiceFacade service;

    @Autowired
    MockMvc mvc;

    @Test
    void findByLatAndLonAndAlt_whenValidRequest_thenResponse() throws Exception {
        WeatherResponseDto expected = weatherResponseDto();
        when(service.findByLatAndLonAndAlt(LAT, LON, ALTITUDE)).thenReturn(expected);

        MvcResult mvcResult = mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_WITH_PARAMS))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        WeatherResponseDto result = jsonToWeatherResponseDto(content);
        assertEquals(expected, result);
        verify(service, timeout(1)).findByLatAndLonAndAlt(LAT, LON, ALTITUDE);
        verifyNoMoreInteractions(service);
    }

    @Test
    void findByLatAndLonAndAlt_whenMissingParams_thenErrorResponse_and400() throws Exception {

        MvcResult mvcResult = mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_MISSING_REQUIRED_PARAMS))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        ErrorDto result = jsonToErrorDto(content);

        assertEquals(result.getCode(), BAD_REQUEST.value());
        assertEquals(result.getMethod(), GET.toString());
        assertThat(result.getMessage()).contains("Required request parameter");
        verifyNoInteractions(service);
    }

    @Test
    void findByLatAndLonAndAlt_whenInvalidParamType_thenErrorResponse_and400() throws Exception {
        MvcResult mvcResult = mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_WRONG_TYPE_PARAMS))
                .andExpect(status().isBadRequest())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        ErrorDto result = jsonToErrorDto(content);

        assertEquals(result.getCode(), BAD_REQUEST.value());
        assertEquals(result.getMethod(), GET.toString());
        assertThat(result.getMessage()).contains("Failed to convert value of type");
        verifyNoInteractions(service);
    }

}
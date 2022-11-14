package com.meawallet.weather.controller.impl.unit;

import com.meawallet.weather.model.ErrorDto;
import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.service.impl.WeatherServiceFacadeImpl;
import com.meawallet.weather.web.controller.impl.WeatherControllerImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.meawallet.weather.test.util.JsonTestUtil.jsonToErrorDto;
import static com.meawallet.weather.test.util.JsonTestUtil.jsonToWeatherResponseDto;
import static com.meawallet.weather.test.util.WeatherTestUtil.PRECISE_ALTITUDE;
import static com.meawallet.weather.test.util.WeatherTestUtil.PRECISE_LAT;
import static com.meawallet.weather.test.util.WeatherTestUtil.PRECISE_LON;
import static com.meawallet.weather.test.util.WeatherTestUtil.WEATHER_CONTROLLER_FIND_URL_MISSING_REQUIRED_PARAMS;
import static com.meawallet.weather.test.util.WeatherTestUtil.WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS;
import static com.meawallet.weather.test.util.WeatherTestUtil.WEATHER_CONTROLLER_FIND_URL_WRONG_TYPE_PARAMS;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherResponseDto;
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
    WeatherServiceFacadeImpl service;

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    void findByLatAndLonAndAlt_whenValidRequest_thenResponse() throws Exception {
        WeatherResponseDto expected = weatherResponseDto();
        when(service.findByLatAndLonAndAlt(PRECISE_LAT, PRECISE_LON, PRECISE_ALTITUDE)).thenReturn(expected);

        MvcResult mvcResult = mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        WeatherResponseDto result = jsonToWeatherResponseDto(content);
        assertEquals(expected, result);
        verify(service, timeout(1)).findByLatAndLonAndAlt(PRECISE_LAT, PRECISE_LON, PRECISE_ALTITUDE);
        verifyNoMoreInteractions(service);
    }

    @Test
    @WithMockUser
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

    @Test()
    @WithMockUser
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
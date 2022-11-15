package com.meawallet.weather.controller.impl.integration;

import com.meawallet.weather.model.ErrorDto;
import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.repository.WeatherRepository;
import com.meawallet.weather.repository.entity.WeatherEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import static com.meawallet.weather.message.store.WeatherApiCallExecutorMessageStore.buildApiCallExceptionMessage;
import static com.meawallet.weather.test.util.JsonTestUtil.jsonToErrorDto;
import static com.meawallet.weather.test.util.JsonTestUtil.jsonToWeatherResponseDto;
import static com.meawallet.weather.test.util.WeatherTestUtil.COMPLETE_NODE_STRING;
import static com.meawallet.weather.test.util.WeatherTestUtil.CURRENT_HOUR_NODE_TEMPERATURE;
import static com.meawallet.weather.test.util.WeatherTestUtil.WEATHER_API_URL_WITH_ALL_PRECISE_PARAMS;
import static com.meawallet.weather.test.util.WeatherTestUtil.WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS;
import static com.meawallet.weather.test.util.WeatherTestUtil.getRequiredHeaders;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherEntityPreciseParams;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(locations = "classpath:config-test.properties")
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = AFTER_EACH_TEST_METHOD)
class WeatherControllerImplIntegrationTest {

    @MockBean
    RestTemplate restTemplate;

    @SpyBean
    WeatherRepository repository;

    @Autowired
    MockMvc mvc;

    @Test
    @WithMockUser
    void findByLatAndLonAndAlt_whenValidAndNotFormattedParams_andFoundInDb_thenResponse() throws Exception {
        WeatherEntity entity = weatherEntityPreciseParams();
        repository.save(entity);
        WeatherResponseDto expected = weatherResponseDto();

        MvcResult mvcResult = mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        WeatherResponseDto result = jsonToWeatherResponseDto(content);

        assertEquals(expected, result);
        verifyNoInteractions(restTemplate);
    }

    @Test
    @WithMockUser
    void findByLatAndLonAndAlt_whenValidAndFormattedParams_andFoundInDb_thenResponse() throws Exception {
        WeatherEntity entity = weatherEntityPreciseParams();
        repository.save(entity);
        WeatherResponseDto expected = weatherResponseDto();

        MvcResult mvcResult = mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        WeatherResponseDto result = jsonToWeatherResponseDto(content);

        assertEquals(expected, result);
        verifyNoInteractions(restTemplate);
    }

    @Test
    @WithMockUser
    void findByLatAndLonAndAlt_whenValidParams_andNotFoundInDb_thenCallApi_saveToDB_thenResponse() throws Exception {
        ResponseEntity<String> responseEntity = ResponseEntity.ok().body(COMPLETE_NODE_STRING);
        when(restTemplate.exchange(
                WEATHER_API_URL_WITH_ALL_PRECISE_PARAMS,
                GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class)
        ).thenReturn(responseEntity);

        WeatherResponseDto expected = new WeatherResponseDto(Float.parseFloat(CURRENT_HOUR_NODE_TEMPERATURE));

        MvcResult mvcResult = mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS))
                .andExpect(status().isOk())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        WeatherResponseDto result = jsonToWeatherResponseDto(content);

        assertEquals(expected, result);
        verify(restTemplate, times(1)).exchange(
                WEATHER_API_URL_WITH_ALL_PRECISE_PARAMS,
                GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    @WithMockUser
    void findByLatAndLonAndAlt_whenRestTemplateThrowsException_thenReturnErrorResponse_and500() throws Exception {
        when(restTemplate.exchange(
                WEATHER_API_URL_WITH_ALL_PRECISE_PARAMS,
                GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class)
        ).thenThrow((new RestClientException("TestErrorMessage")));

        MvcResult mvcResult = mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        ErrorDto result = jsonToErrorDto(content);

        assertEquals(INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals(GET.toString(), result.getMethod());
        assertEquals("/api/v1/weather", result.getUri());
        assertEquals(buildApiCallExceptionMessage("TestErrorMessage"), result.getMessage());
        assertEquals(INTERNAL_SERVER_ERROR.getReasonPhrase(), result.getError());
        assertNotNull(result.getTimeStamp());

        verify(restTemplate, times(1)).exchange(
                WEATHER_API_URL_WITH_ALL_PRECISE_PARAMS,
                GET,
                new HttpEntity<>(null, getRequiredHeaders()),
                String.class);
        verifyNoMoreInteractions(restTemplate);
    }

    @Test
    @WithMockUser
    void findByLatAndLonAndAlt_whenDbThrowsPersistenceException_thenReturnErrorResponse_and500() throws Exception {
        when(repository.findDtoByLatAndLonAndAltitudeAndTimeStamp(any(), any(), any(), any()))
                .thenThrow(new PersistenceException("TestErrorMessage"));

        MvcResult mvcResult = mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS))
                .andExpect(status().isInternalServerError())
                .andReturn();

        String content = mvcResult.getResponse().getContentAsString();
        ErrorDto result = jsonToErrorDto(content);

        assertEquals(INTERNAL_SERVER_ERROR.value(), result.getCode());
        assertEquals(GET.toString(), result.getMethod());
        assertEquals("/api/v1/weather", result.getUri());
        assertThat(result.getMessage()).contains("TestErrorMessage");
        assertEquals(INTERNAL_SERVER_ERROR.getReasonPhrase(), result.getError());
        assertNotNull(result.getTimeStamp());
        verifyNoInteractions(restTemplate);
    }

    @Test
    void findByLatAndLonAndAlt_whenValidRequest_andNoAuthentication_thenReturn401() throws Exception {
        mvc.perform(get(WEATHER_CONTROLLER_FIND_URL_WITH_PRECISE_PARAMS))
                .andExpect(status().is(UNAUTHORIZED.value()));
        verifyNoInteractions(restTemplate);
    }

}
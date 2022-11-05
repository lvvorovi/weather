package com.meawallet.weather.business.service.impl;

import com.meawallet.weather.business.handler.exception.WeatherEntityNotFoundException;
import com.meawallet.weather.business.service.WeatherApiService;
import com.meawallet.weather.business.service.WeatherService;
import com.meawallet.weather.business.util.WeatherServiceUtil;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

import static com.meawallet.weather.business.ConstantsStore.WEATHER_ENTITY_SAVED_LOG;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_FIND_REQUEST;
import static com.meawallet.weather.util.WeatherTestUtil.ALTITUDE;
import static com.meawallet.weather.util.WeatherTestUtil.LAT;
import static com.meawallet.weather.util.WeatherTestUtil.LON;
import static com.meawallet.weather.util.WeatherTestUtil.weatherApiDto;
import static com.meawallet.weather.util.WeatherTestUtil.weatherResponseDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class WeatherServiceFacadeImplTest {

    @Mock
    WeatherService service;
    @Mock
    WeatherApiService apiService;
    @Mock
    WeatherServiceUtil util;
    @InjectMocks
    WeatherServiceFacadeImpl victim;

    @Test
    void findByLatAndLonAndAlt_whenFoundInDb_thenReturnResponse(CapturedOutput output) {
        WeatherResponseDto expected = weatherResponseDto();
        when(util.formatFloatInputData(LAT)).thenReturn(LAT);
        when(util.formatFloatInputData(LON)).thenReturn(LON);
        when(util.formatIntegerInputData(ALTITUDE)).thenReturn(ALTITUDE);
        when(service.findByLatAndLonAndAlt(LAT, LON, ALTITUDE)).thenReturn(expected);

        WeatherResponseDto result = victim.findByLatAndLonAndAlt(LAT, LON, ALTITUDE);

        assertEquals(expected, result);
        assertThat(output.getOut()).contains(String.format(WEATHER_FIND_REQUEST, LAT, LON, ALTITUDE));
        verify(util, times(1)).formatFloatInputData(LAT);
        verify(util, times(1)).formatFloatInputData(LON);
        verify(util, times(1)).formatIntegerInputData(ALTITUDE);
        verify(service, times(1)).findByLatAndLonAndAlt(LAT,LON, ALTITUDE);
        verifyNoMoreInteractions(util, service);
        verifyNoInteractions(apiService);
    }


    @Test
    void findByLatAndLonAndAlt_whenNotFoundInDb_thenCallApiService_save_andReturn(CapturedOutput output) {
        WeatherResponseDto expected = weatherResponseDto();
        WeatherApiDto apiDtoMock = mock(WeatherApiDto.class);
        when(util.formatFloatInputData(LAT)).thenReturn(LAT);
        when(util.formatFloatInputData(LON)).thenReturn(LON);
        when(util.formatIntegerInputData(null)).thenReturn(null);
        when(service.findByLatAndLonAndAlt(LAT, LON, null))
                .thenThrow(new WeatherEntityNotFoundException("message"));
        when(apiService.getByLatAndLonAndAlt(LAT, LON, null)).thenReturn(apiDtoMock);
        doNothing().when(apiDtoMock).setAltitude(null);
        when(service.save(apiDtoMock)).thenReturn(expected);

        WeatherResponseDto result = victim.findByLatAndLonAndAlt(LAT, LON, null);

        assertEquals(expected, result);
        assertThat(output.getOut()).contains(String.format(WEATHER_FIND_REQUEST, LAT, LON, null));
        assertThat(output.getOut()).contains("message");
        verify(util, times(1)).formatFloatInputData(LAT);
        verify(util, times(1)).formatFloatInputData(LON);
        verify(util, times(1)).formatIntegerInputData(null);
        verify(service, times(1)).findByLatAndLonAndAlt(LAT,LON, null);
        verify(apiService, times(1)).getByLatAndLonAndAlt(LAT, LON, null);
        verify(apiDtoMock, times(1)).setAltitude(null);
        verify(service, times(1)).save(apiDtoMock);
        verifyNoMoreInteractions(util, service, apiService, apiDtoMock);
    }




}
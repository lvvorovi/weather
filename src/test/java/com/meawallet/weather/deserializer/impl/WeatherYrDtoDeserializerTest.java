package com.meawallet.weather.deserializer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meawallet.weather.handler.exception.WeatherApiDtoDeserializerException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.test.util.TestJsonProcessingException;
import com.meawallet.weather.util.WeatherDeserializerUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.buildDeserializerFailMessage;
import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.buildDeserializerNullResponseMessage;
import static com.meawallet.weather.test.util.WeatherTestUtil.COMPLETE_NODE_STRING;
import static com.meawallet.weather.test.util.WeatherTestUtil.weatherApiDto;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
class WeatherYrDtoDeserializerTest {

    @Mock
    CustomStdDeserializer deserializer;
    @Mock
    WeatherDeserializerUtil util;

    @InjectMocks
    WeatherYrDtoDeserializer victim;

    @Test
    void deserializeApiResponse_whenMapperReadValidValue_thenReturnDto() throws JsonProcessingException {
        WeatherApiDto expected = weatherApiDto();
        ObjectMapper mapperMock = mock(ObjectMapper.class);
        when(util.getWeatherObjectMapper(deserializer)).thenReturn(mapperMock);
        when(mapperMock.readValue(COMPLETE_NODE_STRING, WeatherApiDto.class)).thenReturn(expected);

        WeatherApiDto result = victim.deserializeApiResponse(COMPLETE_NODE_STRING);

        assertEquals(expected, result);
        verify(util, times(1)).getWeatherObjectMapper(deserializer);
        verify(mapperMock, times(1)).readValue(COMPLETE_NODE_STRING, WeatherApiDto.class);
        verifyNoMoreInteractions(util, mapperMock);
        verifyNoInteractions(deserializer);
    }

    @Test
    void deserializeApiResponse_whenMapperThrowJsonProcessingException_thenThrowWeatherApiDtoDeserializerException() throws JsonProcessingException {
        ObjectMapper mapperMock = mock(ObjectMapper.class);
        when(util.getWeatherObjectMapper(deserializer)).thenReturn(mapperMock);
        when(mapperMock.readValue(COMPLETE_NODE_STRING, WeatherApiDto.class))
                .thenThrow(new TestJsonProcessingException("message"));

        assertThatThrownBy(() -> victim.deserializeApiResponse(COMPLETE_NODE_STRING))
                .isInstanceOf(WeatherApiDtoDeserializerException.class)
                .hasMessage(buildDeserializerFailMessage("message"));

        verify(util, times(1)).getWeatherObjectMapper(deserializer);
        verify(mapperMock, times(1)).readValue(COMPLETE_NODE_STRING, WeatherApiDto.class);
        verifyNoMoreInteractions(util, mapperMock);
        verifyNoInteractions(deserializer);
    }

    @Test
    void deserializeApiResponse_whenMapperReturnNull_thenThrowWeatherApiDtoDeserializerException() throws JsonProcessingException {
        ObjectMapper mapperMock = mock(ObjectMapper.class);
        when(util.getWeatherObjectMapper(deserializer)).thenReturn(mapperMock);
        when(mapperMock.readValue(COMPLETE_NODE_STRING, WeatherApiDto.class)).thenReturn(null);

        assertThatThrownBy(() -> victim.deserializeApiResponse(COMPLETE_NODE_STRING))
                .isInstanceOf(WeatherApiDtoDeserializerException.class)
                .hasMessage(buildDeserializerNullResponseMessage(COMPLETE_NODE_STRING));

        verify(util, times(1)).getWeatherObjectMapper(deserializer);
        verify(mapperMock, times(1)).readValue(COMPLETE_NODE_STRING, WeatherApiDto.class);
        verifyNoMoreInteractions(util, mapperMock);
        verifyNoInteractions(deserializer);
    }


}
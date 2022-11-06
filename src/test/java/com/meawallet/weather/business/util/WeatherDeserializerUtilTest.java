package com.meawallet.weather.business.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.meawallet.weather.business.deserializer.impl.CustomStdDeserializer;
import com.meawallet.weather.model.WeatherApiDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static com.meawallet.weather.util.WeatherTestUtil.ARRAY_NODE_STRING;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherDeserializerUtilTest {

    @InjectMocks
    WeatherDeserializerUtil victim;

    @Test
    void getWeatherObjectMapper_test() {
        ObjectMapper mapper = victim.getWeatherObjectMapper(new CustomStdDeserializer());

        assertTrue(mapper.canSerialize(WeatherApiDto.class));
    }

    @Test
    void getCurrentNode_whenFound_thenReturnedCurrentHourNode() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode = mapper.readTree(ARRAY_NODE_STRING);
        String arrayIndexZero = arrayNode.get(0).get("time").toString();
        String expected = arrayNode.get(1).get("time").toString();

        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(arrayIndexZero, LocalDateTime.class))
                .thenReturn(LocalDateTime.now().truncatedTo(HOURS).minusHours(3));
        when(mockedMapper.readValue(expected, LocalDateTime.class))
                .thenReturn(LocalDateTime.now().truncatedTo(HOURS).minusHours(2));

        JsonNode resultNode = victim.getCurrentNode(arrayNode, mockedMapper, 2L);
        String result = resultNode.get("time").toString();

        assertEquals(expected, result);
        verify(mockedMapper, times(1)).readValue(expected, LocalDateTime.class);
        verify(mockedMapper, times(1)).readValue(arrayIndexZero, LocalDateTime.class);
        verifyNoMoreInteractions(mockedMapper);
    }

    @Test
    void getCurrentNode_whenNotFound_thenReturnIndexZero() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode arrayNode = mapper.readTree(ARRAY_NODE_STRING);
        String expected = arrayNode.get(0).get("time").toString();
        String arrayIndexOne = arrayNode.get(1).get("time").toString();
        ObjectMapper mockedMapper = mock(ObjectMapper.class);
        when(mockedMapper.readValue(expected, LocalDateTime.class))
                .thenReturn(LocalDateTime.now().truncatedTo(HOURS).minusHours(3));
        when(mockedMapper.readValue(arrayIndexOne, LocalDateTime.class))
                .thenReturn(LocalDateTime.now().truncatedTo(HOURS).minusHours(4));

        JsonNode resultNode = victim.getCurrentNode(arrayNode, mockedMapper, 2L);
        String result = resultNode.get("time").toString();

        assertEquals(expected, result);
        verify(mockedMapper, times(1)).readValue(expected, LocalDateTime.class);
        verify(mockedMapper, times(1)).readValue(arrayIndexOne, LocalDateTime.class);
        verifyNoMoreInteractions(mockedMapper);
    }

}
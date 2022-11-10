package com.meawallet.weather.deserializer.impl;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.meawallet.weather.handler.exception.WeatherApiDtoCustomDeserializerException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.properties.WeatherProperties;
import com.meawallet.weather.util.WeatherDeserializerUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.meawallet.weather.message.store.WeatherDeserializerMessageStore.DESERIALIZER_CODEC_READ_FAIL_MESSAGE;
import static com.meawallet.weather.util.WeatherTestUtil.COMPLETE_NODE_STRING;
import static com.meawallet.weather.util.WeatherTestUtil.CURRENT_HOUR_NODE_STRING;
import static com.meawallet.weather.util.WeatherTestUtil.CURRENT_HOUR_NODE_TEMPERATURE;
import static com.meawallet.weather.util.WeatherTestUtil.PRECISE_ALTITUDE;
import static com.meawallet.weather.util.WeatherTestUtil.PRECISE_LAT;
import static com.meawallet.weather.util.WeatherTestUtil.PRECISE_LON;
import static com.meawallet.weather.util.WeatherTestUtil.stringToJsonNode;
import static java.time.temporal.ChronoUnit.HOURS;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class CustomStdDeserializerTest {

    @Mock
    WeatherProperties properties;
    @Mock
    WeatherDeserializerUtil util;
    @InjectMocks
    CustomStdDeserializer victim;

    @BeforeEach
    void setup() {
        openMocks(this);
    }

    @Test
    void deserialize_whenValidJson_thenDeserialize() throws Exception {
        JsonNode currentHourNode = stringToJsonNode(CURRENT_HOUR_NODE_STRING);
        JsonNode completeNode = stringToJsonNode(COMPLETE_NODE_STRING);
        JsonParser parserMock = mock(JsonParser.class);
        DeserializationContext contextMock = mock(DeserializationContext.class);
        ObjectCodec codecMock = mock(ObjectCodec.class);
        when(parserMock.getCodec()).thenReturn(codecMock);
        when(codecMock.readTree(parserMock)).thenReturn(completeNode);
        when(properties.getTimeDifference()).thenReturn(2L);
        when(util.getCurrentNode(any(), any(), anyLong())).thenReturn(currentHourNode);

        WeatherApiDto result = victim.deserialize(parserMock, contextMock);

        assertNotNull(result);
        assertEquals(PRECISE_ALTITUDE, result.getAltitude());
        assertEquals(PRECISE_LAT, result.getLat());
        assertEquals(PRECISE_LON, result.getLon());
        assertEquals(CURRENT_HOUR_NODE_TEMPERATURE, result.getTemperature().toString());
        assertEquals(LocalDateTime.now().truncatedTo(HOURS), result.getTimeStamp());
    }

    @Test
    void deserialize_test() throws Exception {
        JsonParser parserMock = mock(JsonParser.class);
        DeserializationContext contextMock = mock(DeserializationContext.class);
        ObjectCodec codecMock = mock(ObjectCodec.class);
        when(parserMock.getCodec()).thenReturn(codecMock);
        when(codecMock.readTree(parserMock)).thenThrow(new IOException("message"));

        assertThatThrownBy(() -> victim.deserialize(parserMock, contextMock))
                .isInstanceOf(WeatherApiDtoCustomDeserializerException.class)
                .hasMessage(DESERIALIZER_CODEC_READ_FAIL_MESSAGE);
    }

}
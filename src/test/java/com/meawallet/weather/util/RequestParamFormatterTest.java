package com.meawallet.weather.util;

import com.meawallet.weather.properties.WeatherProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class})
@ActiveProfiles("test.properties")
class RequestParamFormatterTest {

    @Mock
    WeatherProperties properties;
    @InjectMocks
    RequestParamFormatter victim;

    @Test
    void formatLatValue_whenNotFormatted_thenReturnSameValue() {
        Float expected = 12.0321F;
        when(properties.getApiLatMaxDecimalValue()).thenReturn(4);

        Float result = victim.formatLatValue(expected);

        assertEquals(expected, result);
        verify(properties, times(1)).getApiLatMaxDecimalValue();
        verifyNoMoreInteractions(properties);
    }

    @Test
    void formatLatValue_whenFormatted_thenReturnFormattedValue() {
        Float expected = 12.03210321F;
        when(properties.getApiLatMaxDecimalValue()).thenReturn(4);


        Float result = victim.formatLatValue(expected);

        assertNotEquals(expected, result);
        verify(properties, times(1)).getApiLatMaxDecimalValue();
        verifyNoMoreInteractions(properties);
    }

    @Test
    void formatLonValue_whenNotFormatted_thenReturnSameValue() {
        Float expected = 12.0321F;
        when(properties.getApiLonMaxDecimalValue()).thenReturn(4);

        Float result = victim.formatLonValue(expected);

        assertEquals(expected, result);
        verify(properties, times(1)).getApiLonMaxDecimalValue();
        verifyNoMoreInteractions(properties);
    }

    @Test
    void formatLonValue_whenFormatted_thenReturnFormattedValue() {
        Float expected = 12.03210321F;
        when(properties.getApiLonMaxDecimalValue()).thenReturn(4);

        Float result = victim.formatLonValue(expected);

        assertNotEquals(expected, result);
        verify(properties, times(1)).getApiLonMaxDecimalValue();
        verifyNoMoreInteractions(properties);
    }

}
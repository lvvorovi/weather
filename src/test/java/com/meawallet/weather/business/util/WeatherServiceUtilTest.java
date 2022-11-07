package com.meawallet.weather.business.util;

import com.meawallet.weather.properties.WeatherProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
@ActiveProfiles("test.properties")
class WeatherServiceUtilTest {

    @Mock
    WeatherProperties properties;
    @InjectMocks
    WeatherServiceUtil victim;

    @Test
    void formatLatValue_whenNotFormatted_thenReturnSameValue(CapturedOutput output) {
        Float expected = 12.0321F;
        when(properties.getApiLatMaxDecimalValue()).thenReturn(4);

        Float result = victim.formatLatValue(expected);

        assertEquals(expected, result);
        assertThat(output.getOut()).isEmpty();
        verify(properties, times(1)).getApiLatMaxDecimalValue();
        verifyNoMoreInteractions(properties);
    }

    @Test
    void formatLatValue_whenFormatted_thenReturnFormattedValue(CapturedOutput output) {
        Float expected = 12.03210321F;
        when(properties.getApiLatMaxDecimalValue()).thenReturn(4);


        Float result = victim.formatLatValue(expected);

        assertNotEquals(expected, result);
        assertThat(output.getOut()).contains(expected.toString());
        assertThat(output.getOut()).contains(result.toString());
        verify(properties, times(1)).getApiLatMaxDecimalValue();
        verifyNoMoreInteractions(properties);
    }

    @Test
    void formatLonValue_whenNotFormatted_thenReturnSameValue(CapturedOutput output) {
        Float expected = 12.0321F;
        when(properties.getApiLonMaxDecimalValue()).thenReturn(4);

        Float result = victim.formatLonValue(expected);

        assertEquals(expected, result);
        assertThat(output.getOut()).isEmpty();
        verify(properties, times(1)).getApiLonMaxDecimalValue();
        verifyNoMoreInteractions(properties);
    }

    @Test
    void formatLonValue_whenFormatted_thenReturnFormattedValue(CapturedOutput output) {
        Float expected = 12.03210321F;
        when(properties.getApiLonMaxDecimalValue()).thenReturn(4);

        Float result = victim.formatLonValue(expected);

        assertNotEquals(expected, result);
        assertThat(output.getOut()).contains(expected.toString());
        assertThat(output.getOut()).contains(result.toString());
        verify(properties, times(1)).getApiLonMaxDecimalValue();
        verifyNoMoreInteractions(properties);
    }

}
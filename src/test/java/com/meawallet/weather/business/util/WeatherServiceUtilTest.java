package com.meawallet.weather.business.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
@ActiveProfiles("test.properties")
class WeatherServiceUtilTest {

    @InjectMocks
    WeatherServiceUtil victim;

    @Test
    void formatFloatInputData_whenNotFormatted_thenReturnSameValue(CapturedOutput output) {
        Float expected = 12.0321F;

        Float result = victim.formatFloatInputData(expected);

        assertEquals(expected, result);
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void formatFloatInputData_whenFormatted_thenReturnFormattedValue(CapturedOutput output) {
        Float expected = 12.03210321F;

        Float result = victim.formatFloatInputData(expected);

        assertNotEquals(expected, result);
        assertThat(output.getOut()).contains(expected.toString());
        assertThat(output.getOut()).contains(result.toString());
    }

    @Test
    void formatIntegerInputData_whenNotFormatted_thenReturnSameValue(CapturedOutput output) {
        Integer expected = 100;

        Integer result = victim.formatIntegerInputData(expected);

        assertEquals(expected, result);
        assertThat(output.getOut()).isEmpty();
    }

    @Test
    void formatIntegerInputData_whenFormatted_thenReturnFormattedValue(CapturedOutput output) {
        Integer expected = -1500;

        Integer result = victim.formatIntegerInputData(expected);

        assertNotEquals(expected, result);
        assertThat(output.getOut()).contains(expected.toString());
        assertThat(output.getOut()).contains(result.toString());
    }

    @Test
    void formatIntegerInputData_whenNull_thenReturnNull(CapturedOutput output) {
        Integer expected = null;

        Integer result = victim.formatIntegerInputData(expected);

        assertEquals(expected, result);
        assertThat(output.getOut()).isEmpty();
    }

}
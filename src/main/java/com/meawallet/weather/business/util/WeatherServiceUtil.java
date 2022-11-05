package com.meawallet.weather.business.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;

import static com.meawallet.weather.business.ConstantsStore.FLOAT_VALUE_WAS_ADJUSTED_LOG;
import static com.meawallet.weather.business.ConstantsStore.INTEGER_VALUE_WAS_ADJUSTED_LOG;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_CONTROLLER_ALTITUDE_MAX_VALUE;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_CONTROLLER_ALTITUDE_MIN_VALUE;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_CONTROLLER_LAT_AND_LON_MAX_DECIMAL_NUMBER;

@Component
@Slf4j
public class WeatherServiceUtil {

    public Float formatFloatInputData(Float value) {
        DecimalFormat formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(WEATHER_CONTROLLER_LAT_AND_LON_MAX_DECIMAL_NUMBER);
        Float formattedValue = Float.parseFloat(formatter.format(value));

        if (log.isDebugEnabled() && !value.equals(formattedValue)) {
            log.debug(FLOAT_VALUE_WAS_ADJUSTED_LOG, value, formattedValue);
        }

        return formattedValue;
    }

    public Integer formatIntegerInputData(Integer value) {
        if (value == null) {
            return null;
        }

        int formattedValue = Math.min(value, WEATHER_CONTROLLER_ALTITUDE_MAX_VALUE);
        formattedValue = Math.max(formattedValue, WEATHER_CONTROLLER_ALTITUDE_MIN_VALUE);

        if (log.isDebugEnabled() && !value.equals(formattedValue)) {
            log.debug(INTEGER_VALUE_WAS_ADJUSTED_LOG, value, formattedValue);
        }

        return formattedValue;
    }

}

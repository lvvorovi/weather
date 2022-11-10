package com.meawallet.weather.util;

import com.meawallet.weather.properties.WeatherProperties;
import com.meawallet.weather.message.store.WeatherServiceMessageStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;


@Component
@Slf4j
@RequiredArgsConstructor
public class RequestParamFormatter {

    private final WeatherProperties properties;

    public Float formatLatValue(Float lat) {
        DecimalFormat formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(properties.getApiLatMaxDecimalValue());
        float formattedLat = Float.parseFloat(formatter.format(lat));

        if (log.isDebugEnabled() && !lat.equals(formattedLat)) {
            log.debug(WeatherServiceMessageStore.buildLatValueWasAdjustedMessage(lat, formattedLat));
        }

        return formattedLat;
    }

    public Float formatLonValue(Float lon) {
        DecimalFormat formatter = new DecimalFormat();
        formatter.setMaximumFractionDigits(properties.getApiLonMaxDecimalValue());
        float formattedLon = Float.parseFloat(formatter.format(lon));

        if (log.isDebugEnabled() && !lon.equals(formattedLon)) {
            log.debug(WeatherServiceMessageStore.buildLonValueWasAdjustedMessage(lon, formattedLon));
        }

        return formattedLon;
    }

}

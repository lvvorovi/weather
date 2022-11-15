package com.meawallet.weather.util;

import com.meawallet.weather.payload.YrApiServiceRequestDto;
import com.meawallet.weather.properties.WeatherProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.Map;

import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.WEATHER_API_PARAM_ALTITUDE;
import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.WEATHER_API_PARAM_LAT;
import static com.meawallet.weather.message.store.WeatherApiServiceMessageStore.WEATHER_API_PARAM_LON;
import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.USER_AGENT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
@RequiredArgsConstructor
public class YrServiceRequestBuilder {

    private final WeatherProperties properties;

    public YrApiServiceRequestDto build(Float lat, Float lon, Integer alt, HttpMethod httpMethod) {
        return YrApiServiceRequestDto.builder()
                .httpEntity(buildHttpEntity(properties.getUserAgentHeaderValue()))
                .httpMethod(httpMethod)
                .url(buildRequestUrl(properties.getApiUrlCompact(), buildUrlParams(lat, lon, alt)))
                .build();
    }

    private Map<String, String> buildUrlParams(Float lat, Float lon, Integer alt) {
        Map<String, String> params = new HashMap<>();
        params.put(WEATHER_API_PARAM_LAT, lat.toString());
        params.put(WEATHER_API_PARAM_LON, lon.toString());

        if (alt != null) {
            params.put(WEATHER_API_PARAM_ALTITUDE, alt.toString());
        }

        return params;
    }

    private String buildRequestUrl(String url, Map<String, String> params) {
        String altitudeNullUrl = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam(WEATHER_API_PARAM_LAT, params.get(WEATHER_API_PARAM_LAT))
                .queryParam(WEATHER_API_PARAM_LON, params.get(WEATHER_API_PARAM_LON))
                .encode()
                .toUriString();

        if (params.containsKey(WEATHER_API_PARAM_ALTITUDE)) {
            return UriComponentsBuilder.fromHttpUrl(altitudeNullUrl)
                    .queryParam(WEATHER_API_PARAM_ALTITUDE, params.get(WEATHER_API_PARAM_ALTITUDE))
                    .encode()
                    .toUriString();
        } else {
            return altitudeNullUrl;
        }
    }

    private HttpHeaders buildRequiredHeaders(String userAgentHeaderValue) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(USER_AGENT, userAgentHeaderValue);
        headers.add(ACCEPT, APPLICATION_JSON_VALUE);
        return headers;
    }

    private HttpEntity<Null> buildHttpEntity(String userAgentValue) {
        return new HttpEntity<>(null, buildRequiredHeaders(userAgentValue));
    }
}
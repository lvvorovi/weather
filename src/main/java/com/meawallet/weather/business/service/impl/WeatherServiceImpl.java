package com.meawallet.weather.business.service.impl;

import com.meawallet.weather.business.handler.exception.WeatherEntityNotFoundException;
import com.meawallet.weather.business.mapper.WeatherMapper;
import com.meawallet.weather.business.repository.WeatherRepository;
import com.meawallet.weather.business.repository.entity.WeatherEntity;
import com.meawallet.weather.business.service.WeatherService;
import com.meawallet.weather.business.validation.service.WeatherValidationService;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.meawallet.weather.business.ConstantsStore.WEATHER_ENTITY_DELETED_LOG;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_ENTITY_FOUND_LOG;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_ENTITY_NOT_FOUND_MESSAGE;
import static com.meawallet.weather.business.ConstantsStore.WEATHER_ENTITY_SAVED_LOG;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository repository;
    private final WeatherMapper mapper;
    private final WeatherValidationService validationService;
    @Value("${weather.entity.ttl.hours}")
    private int entityTtl;

    @Override
    public WeatherResponseDto findByLatAndLonAndAlt(Float lat, Float lon, Integer altitude) {
        LocalDateTime dateTime = LocalDateTime.now().truncatedTo(HOURS);

        WeatherEntity entity = repository.findByLatAndLonAndAltitudeAndTimeStamp(lat, lon, altitude, dateTime)
                .orElseThrow(() -> new WeatherEntityNotFoundException(
                        String.format(WEATHER_ENTITY_NOT_FOUND_MESSAGE, lat, lon, altitude)));

        log.info(String.format(WEATHER_ENTITY_FOUND_LOG, entity.getId()));

        return mapper.entityToDto(entity);
    }

    @Override
    public WeatherResponseDto save(WeatherApiDto requestDto) {
        validationService.validate(requestDto);
        WeatherEntity requestEntity = mapper.dtoToEntity(requestDto);
        requestEntity.setId(UUID.randomUUID().toString());
        WeatherEntity savedEntity = repository.save(requestEntity);
        log.info(String.format(WEATHER_ENTITY_SAVED_LOG, savedEntity.getId()));
        return mapper.entityToDto(savedEntity);
    }

    @Override
    @Scheduled(cron = "${weather.scheduling.delete.cron}")
    public void deleteOutdated() {
        List<WeatherEntity> deletedEntityList = repository
                .deleteByTimeStampBefore(LocalDateTime.now().minusHours(entityTtl));

        if (log.isDebugEnabled()) {
            deletedEntityList.forEach(entity -> log.debug(WEATHER_ENTITY_DELETED_LOG + entity));
        }
    }
}

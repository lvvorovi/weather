package com.meawallet.weather.business.service.impl;

import com.meawallet.weather.business.mapper.WeatherMapper;
import com.meawallet.weather.business.repository.WeatherRepository;
import com.meawallet.weather.business.repository.entity.WeatherEntity;
import com.meawallet.weather.business.service.WeatherService;
import com.meawallet.weather.business.validation.service.WeatherValidationService;
import com.meawallet.weather.handler.exception.WeatherEntityNotFoundException;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.properties.WeatherProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.meawallet.weather.business.message.store.WeatherServiceMessageStore.buildDeletedMessage;
import static com.meawallet.weather.business.message.store.WeatherServiceMessageStore.buildFoundMessage;
import static com.meawallet.weather.business.message.store.WeatherServiceMessageStore.buildNotFoundMessage;
import static com.meawallet.weather.business.message.store.WeatherServiceMessageStore.buildSavedMessage;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private final WeatherRepository repository;
    private final WeatherMapper mapper;
    private final WeatherValidationService validationService;
    private final WeatherProperties properties;

    @Override
    public WeatherResponseDto findDtoByLatAndLonAndAlt(Float lat, Float lon, Integer altitude) {
        LocalDateTime now = LocalDateTime.now().truncatedTo(HOURS);

        WeatherEntity entity = repository.findByLatAndLonAndAltitudeAndTimeStamp(lat, lon, altitude, now)
                .orElseThrow(() ->
                        new WeatherEntityNotFoundException(buildNotFoundMessage(lat, lon, altitude, now)));

        log.info(buildFoundMessage(lat, lon, altitude, now));

        return mapper.entityToDto(entity);
    }

    @Override
    public WeatherResponseDto save(WeatherApiDto requestDto) {
        validationService.validate(requestDto);
        WeatherEntity requestEntity = mapper.dtoToEntity(requestDto);
        requestEntity.setId(UUID.randomUUID().toString());
        WeatherEntity savedEntity = repository.save(requestEntity);
        log.info(buildSavedMessage(savedEntity.getId()));
        return mapper.entityToDto(savedEntity);
    }

    @Override
    @Scheduled(cron = "${weather.scheduling-delete-cron}")
    public void deleteOutdated() {
        List<WeatherEntity> deletedEntityList = repository
                .deleteByTimeStampBefore(LocalDateTime.now().minusHours(properties.getEntityTtlHours()));

        if (log.isDebugEnabled()) {
            deletedEntityList.forEach(entity -> log.debug(buildDeletedMessage(entity.getId())));
        }
    }
}

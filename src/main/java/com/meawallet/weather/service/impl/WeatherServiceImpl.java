package com.meawallet.weather.service.impl;

import com.meawallet.weather.handler.exception.WeatherEntityAlreadyExistsException;
import com.meawallet.weather.handler.exception.WeatherEntityNotFoundException;
import com.meawallet.weather.mapper.WeatherMapper;
import com.meawallet.weather.model.WeatherApiDto;
import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.properties.WeatherProperties;
import com.meawallet.weather.repository.WeatherRepository;
import com.meawallet.weather.repository.entity.WeatherEntity;
import com.meawallet.weather.service.WeatherService;
import com.meawallet.weather.validation.service.WeatherValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.meawallet.weather.message.store.WeatherServiceMessageStore.buildAlreadyExistsMessage;
import static com.meawallet.weather.message.store.WeatherServiceMessageStore.buildDeletedMessage;
import static com.meawallet.weather.message.store.WeatherServiceMessageStore.buildDtoFoundMessage;
import static com.meawallet.weather.message.store.WeatherServiceMessageStore.buildDtoNotFoundMessage;
import static com.meawallet.weather.message.store.WeatherServiceMessageStore.buildSavedMessage;
import static java.time.temporal.ChronoUnit.HOURS;

@Service
@RequiredArgsConstructor
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private static final LocalDateTime NOW = LocalDateTime.now();
    public static final LocalDateTime NOW_TRUNCATED_TO_HOURS = NOW.truncatedTo(HOURS);

    private final WeatherRepository repository;
    private final WeatherMapper mapper;
    private final WeatherValidationService validationService;
    private final WeatherProperties properties;

    @Override
    public WeatherResponseDto findDtoByLatAndLonAndAlt(Float lat, Float lon, Integer altitude) {
        WeatherResponseDto responseDto = repository
                .findDtoByLatAndLonAndAltitudeAndTimeStamp(lat, lon, altitude, NOW_TRUNCATED_TO_HOURS)
                .orElseThrow(() -> new WeatherEntityNotFoundException(
                        buildDtoNotFoundMessage(lat, lon, altitude, NOW_TRUNCATED_TO_HOURS)));

        log.info(buildDtoFoundMessage(lat, lon, altitude, NOW_TRUNCATED_TO_HOURS));
        return responseDto;
    }

    @Override
    public WeatherResponseDto save(WeatherApiDto requestDto) {
        validationService.validate(requestDto);
        WeatherEntity requestEntity = mapper.dtoToEntity(requestDto);
        requestEntity.setId(UUID.randomUUID().toString());
        WeatherEntity savedEntity;

        try {
            savedEntity = repository.save(requestEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new WeatherEntityAlreadyExistsException(buildAlreadyExistsMessage(requestEntity));
        }

        log.info(buildSavedMessage(savedEntity.getId()));
        return mapper.entityToDto(savedEntity);
    }

    @Override
    @Scheduled(cron = "${weather.scheduling-delete-cron}")
    public void deleteOutdated() {
        if (!properties.getIsMasterJvm()) return;

        List<WeatherEntity> deletedEntityList = repository
                .deleteByTimeStampBefore(NOW.minusHours(properties.getEntityTtlHours()));

        if (!deletedEntityList.isEmpty()) {
            log.debug(buildDeletedMessage(deletedEntityList));
        }
    }
}

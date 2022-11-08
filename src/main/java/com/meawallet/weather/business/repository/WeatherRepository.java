package com.meawallet.weather.business.repository;

import com.meawallet.weather.business.repository.entity.WeatherEntity;
import com.meawallet.weather.model.WeatherResponseDto;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeatherRepository extends CrudRepository<WeatherEntity, String> {

    Optional<WeatherEntity> findByLatAndLonAndAltitudeAndTimeStamp(Float lat, Float lon, Integer altitude,
                                                                   LocalDateTime timeStamp);

    @Query("""
            SELECT new com.meawallet.weather.model.WeatherResponseDto(w.temperature) FROM WeatherEntity w
            """)
    List<WeatherResponseDto> findAllDto();

    boolean existsByLatAndLonAndAltitudeAndTimeStamp(Float lat, Float lon, Integer altitude, LocalDateTime timeStamp);

    @Transactional
    List<WeatherEntity> deleteByTimeStampBefore(LocalDateTime timeStamp);
}

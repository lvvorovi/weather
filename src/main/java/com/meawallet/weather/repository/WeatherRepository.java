package com.meawallet.weather.repository;

import com.meawallet.weather.model.WeatherResponseDto;
import com.meawallet.weather.repository.entity.WeatherEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WeatherRepository extends CrudRepository<WeatherEntity, String> {

    @Query("""
            SELECT new com.meawallet.weather.model.WeatherResponseDto(w.temperature) FROM WeatherEntity w
            WHERE
            w.lat= :lat AND
            w.lon= :lon AND
            w.timeStamp= :timeStamp AND
            w.altitude= :altitude OR (:altitude IS NULL AND w.altitude IS NULL)
            """)
    Optional<WeatherResponseDto> findDtoByLatAndLonAndAltitudeAndTimeStamp(
            @Param("lat") Float lat,
            @Param("lon") Float lon,
            @Param("altitude") Integer altitude,
            @Param("timeStamp") LocalDateTime timeStamp);

    @Transactional
    List<WeatherEntity> deleteByTimeStampBefore(LocalDateTime timeStamp);
}

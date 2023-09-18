package ru.practicum.explore.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.models.EndpointHit;
import ru.practicum.explore.models.HitEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class HitEntityMapper {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public HitEntity mapToHit(EndpointHit endpointHit) {
        return new HitEntity(endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                LocalDateTime.parse(endpointHit.getTimestamp(), formatter));
    }

    public EndpointHit mapToEndpointHit(HitEntity hitEntity) {
        return new EndpointHit(hitEntity.getId(),
                hitEntity.getApp(),
                hitEntity.getUri(),
                hitEntity.getIp(),
                hitEntity.getTimestamp().format(formatter));
    }
}
package ru.practicum.explore.service;

import ru.practicum.explore.model.EndpointHit;
import ru.practicum.explore.model.ViewStats;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EndpointHitService {
    void create(EndpointHit endpointHit);

    Collection<ViewStats> readAll(LocalDateTime start, LocalDateTime end, Collection<String> uris, boolean unique);

}
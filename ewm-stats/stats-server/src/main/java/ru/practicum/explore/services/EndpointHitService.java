package ru.practicum.explore.services;

import ru.practicum.explore.models.EndpointHit;
import ru.practicum.explore.models.ViewStats;

import java.util.List;

public interface EndpointHitService {
    EndpointHit create(EndpointHit endpointHit);

    List<ViewStats> readAll(String start, String end, List<String> uris, boolean unique);

}
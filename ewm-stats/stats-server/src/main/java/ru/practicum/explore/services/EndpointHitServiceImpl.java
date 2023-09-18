package ru.practicum.explore.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.InvalidRequestException;
import ru.practicum.explore.mappers.HitEntityMapper;
import ru.practicum.explore.models.EndpointHit;
import ru.practicum.explore.models.HitEntity;
import ru.practicum.explore.models.ViewStats;
import ru.practicum.explore.repositories.EndpointHitRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public EndpointHit create(EndpointHit endpointHit) {
        HitEntity hit = HitEntityMapper.mapToHit(endpointHit);

        return HitEntityMapper.mapToEndpointHit(endpointHitRepository.save(hit));
    }

    @Override
    public List<ViewStats> readAll(String startDate, String endDate, List<String> uris, boolean unique) {
        LocalDateTime start = LocalDateTime.parse(decode(startDate), formatter);
        LocalDateTime end = LocalDateTime.parse(decode(endDate), formatter);

        if (start.isAfter(end)) {
            throw new InvalidRequestException("The dates of the range are specified incorrectly");
        }

        List<ViewStats> viewStats;

        if (uris == null || uris.isEmpty()) {
            if (unique) {
                viewStats = endpointHitRepository.readStatsWithUniqueViews(start, end);
            } else {
                viewStats = endpointHitRepository.readAllStats(start, end);
            }
        } else {
            if (unique) {
                viewStats = endpointHitRepository.readStatsWithUrisAndUniqueViews(start, end, uris);
            } else {
                viewStats = endpointHitRepository.readStatsWithUris(start, end, uris);
            }
        }

        return viewStats;
    }

    private String decode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
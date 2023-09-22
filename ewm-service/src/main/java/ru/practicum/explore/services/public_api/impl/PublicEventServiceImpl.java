package ru.practicum.explore.services.public_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.StatsClient;
import ru.practicum.explore.component.DataFinder;
import ru.practicum.explore.dto.ReadEventsParams;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.enums.EventLifecycle;
import ru.practicum.explore.exceptions.InvalidRequestException;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.mappers.EventMapper;
import ru.practicum.explore.models.EndpointHit;
import ru.practicum.explore.models.Event;
import ru.practicum.explore.models.ViewStats;
import ru.practicum.explore.repositories.EventRepository;
import ru.practicum.explore.services.public_api.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final DataFinder dataFinder;
    private final StatsClient statsClient;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public Collection<EventShortDto> readAllEvents(ReadEventsParams params, Pageable pageable) {
        if (params.getRangeStart() != null && params.getRangeEnd() != null) {
            if (params.getRangeStart().isAfter(params.getRangeEnd())) {
                throw new InvalidRequestException("The dates of the range are specified incorrectly");
            }
        }

        Collection<Event> events = eventRepository.findEventsByUserParameters(
                params.getText(),
                params.getCategories(),
                params.getPaid(),
                params.getRangeStart(),
                params.getRangeEnd(),
                pageable
        ).getContent();

        if (params.getOnlyAvailable()) {
            events = events.stream()
                    .filter(event -> event.getConfirmedRequests() < event.getParticipantLimit())
                    .collect(Collectors.toList());
        }

        if (events.isEmpty()) {
            return new ArrayList<>();
        }

        statsClient.create(new EndpointHit("ewm-main-service",
                params.getRequest().getRequestURI(),
                params.getRequest().getRemoteAddr(),
                LocalDateTime.now().format(formatter)));

        return events.stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto readEvent(Long eventId, HttpServletRequest request) {
        Event event = dataFinder.findEventById(eventId);

        if (!event.getState().equals(EventLifecycle.PUBLISHED.toString())) {
            throw new NotFoundException("Event must be published");
        }

        statsClient.create(new EndpointHit("ewm-main-service", request.getRequestURI(), request.getRemoteAddr(),
                LocalDateTime.now().format(formatter)));
        event.setViews(countViews(request));
        eventRepository.save(event);

        return EventMapper.mapToEventFullDto(event);

    }

    private long countViews(HttpServletRequest request) {
        List<ViewStats> stats = statsClient.readAll(
                LocalDateTime.now().minusYears(100).format(formatter),
                LocalDateTime.now().plusHours(1).format(formatter),
                List.of(request.getRequestURI()),
                true);

        return !stats.isEmpty() ? stats.get(0).getHits() : 0;
    }
}
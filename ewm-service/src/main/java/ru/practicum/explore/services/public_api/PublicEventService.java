package ru.practicum.explore.services.public_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.ReadEventsParams;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;

public interface PublicEventService {
    Collection<EventShortDto> readAllEvents(ReadEventsParams params, Pageable pageable);

    EventFullDto readEvent(Long eventId, HttpServletRequest request);
}
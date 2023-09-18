package ru.practicum.explore.services.public_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface PublicEventService {
    Collection<EventShortDto> readAllEvents(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                            LocalDateTime rangeEnd, Boolean onlyAvailable,
                                            Pageable pageable, String ip, String uri);

    EventFullDto readEvent(Long eventId, HttpServletRequest request);
}
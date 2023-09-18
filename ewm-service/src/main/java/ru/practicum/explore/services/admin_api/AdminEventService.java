package ru.practicum.explore.services.admin_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.UpdateEventAdminRequest;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface AdminEventService {
    Collection<EventFullDto> readAllEvents(List<Long> users, List<String> states, List<Long> categories,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);
}
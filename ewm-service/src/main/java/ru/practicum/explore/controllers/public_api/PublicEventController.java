package ru.practicum.explore.controllers.public_api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.ReadEventsParams;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.exceptions.InvalidRequestException;
import ru.practicum.explore.services.public_api.PublicEventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/events")
public class PublicEventController {
    private final PublicEventService publicEventService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<EventShortDto> readAllEvents(@RequestParam(required = false) String text,
                                                   @RequestParam(required = false) List<Long> categories,
                                                   @RequestParam(required = false) Boolean paid,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                   LocalDateTime rangeStart,
                                                   @RequestParam(required = false)
                                                   @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                   LocalDateTime rangeEnd,
                                                   @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                                   @RequestParam(required = false) String sort,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size,
                                                   HttpServletRequest request) {
        Pageable pageable = PageRequest.of(from / size, size);

        if (sort != null) {
            if (sort.equals("EVENT_DATE")) {
                pageable = PageRequest.of(from / size, size, Sort.by("eventDate"));
            } else if (sort.equals("VIEWS")) {
                pageable = PageRequest.of(from / size, size, Sort.by("views"));
            } else {
                throw new InvalidRequestException("Incorrect sorting");
            }
        }

        return publicEventService.readAllEvents(new ReadEventsParams(text, categories, paid, rangeStart,
                rangeEnd, onlyAvailable, request), pageable);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventFullDto readEvent(@PathVariable Long eventId, HttpServletRequest request) {
        return publicEventService.readEvent(eventId, request);
    }
}
package ru.practicum.explore.services.admin_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.UpdateEventAdminRequest;
import ru.practicum.explore.enums.AdminEventState;
import ru.practicum.explore.enums.EventLifecycle;
import ru.practicum.explore.exceptions.InvalidRequestException;
import ru.practicum.explore.exceptions.ValidationException;
import ru.practicum.explore.mappers.EventMapper;
import ru.practicum.explore.models.Event;
import ru.practicum.explore.models.Location;
import ru.practicum.explore.repositories.EventRepository;
import ru.practicum.explore.repositories.LocationRepository;
import ru.practicum.explore.services.admin_api.AdminEventService;
import ru.practicum.explore.util.DataFinder;
import ru.practicum.explore.util.UtilMergeProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final DataFinder dataFinder;

    @Override
    public Collection<EventFullDto> readAllEvents(List<Long> initiators,
                                                  List<String> states,
                                                  List<Long> categories,
                                                  LocalDateTime rangeStart,
                                                  LocalDateTime rangeEnd,
                                                  Pageable pageable) {

        Collection<Event> events = eventRepository.findEventsByAdminParameters(initiators, states, categories,
                rangeStart, rangeEnd, pageable).getContent();

        if (events.isEmpty()) {
            return new ArrayList<>();
        }

        return events.stream()
                .map(EventMapper::mapToEventFullDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateEvent(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        Event eventToUpdate = dataFinder.findEventById(eventId);
        LocalDateTime newEventDate = updateEventAdminRequest.getEventDate();

        if (newEventDate != null) {
            if (newEventDate.isBefore(LocalDateTime.now())) {
                throw new InvalidRequestException("Cannot create the event because event date cannot be earlier " +
                        "than current moment");
            }

            LocalDateTime publishDate = eventToUpdate.getPublishedOn();

            if (newEventDate.isBefore(publishDate.minusHours(1))) {
                throw new InvalidRequestException("Cannot update the event date because it is earlier than the " +
                        "publication date more than an hour");
            }
        }

        if (updateEventAdminRequest.getStateAction() != null) {
            if (EventLifecycle.valueOf(eventToUpdate.getState()) != EventLifecycle.PENDING) {
                throw new ValidationException("Cannot publish or reject the event because it's not " +
                        "in the right state: " + eventToUpdate.getState());
            }

            if (updateEventAdminRequest.getStateAction() == AdminEventState.PUBLISH_EVENT) {
                eventToUpdate.setState(EventLifecycle.PUBLISHED.toString());
                eventToUpdate.setPublishedOn(LocalDateTime.now());
            } else {
                eventToUpdate.setState(EventLifecycle.CANCELED.toString());
            }
        }

        if (updateEventAdminRequest.getLocation() != null) {
            Location location = locationRepository.save(updateEventAdminRequest.getLocation());
            eventToUpdate.setLocation(location);
        }

        UtilMergeProperty.copyProperties(updateEventAdminRequest, eventToUpdate);

        return EventMapper.mapToEventFullDto(eventRepository.save(eventToUpdate));
    }
}
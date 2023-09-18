package ru.practicum.explore.services.private_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.dto.event.NewEventDto;
import ru.practicum.explore.dto.event.UpdateEventUserRequest;
import ru.practicum.explore.dto.event_request.EventRequestStatusUpdateRequest;
import ru.practicum.explore.dto.event_request.EventRequestStatusUpdateResult;
import ru.practicum.explore.dto.event_request.ParticipationRequestDto;
import ru.practicum.explore.enums.EventLifecycle;
import ru.practicum.explore.enums.RequestStatus;
import ru.practicum.explore.enums.UserEventState;
import ru.practicum.explore.exceptions.ConditionMismatchException;
import ru.practicum.explore.exceptions.InvalidRequestException;
import ru.practicum.explore.mappers.EventMapper;
import ru.practicum.explore.mappers.EventRequestMapper;
import ru.practicum.explore.models.*;
import ru.practicum.explore.repositories.EventRepository;
import ru.practicum.explore.repositories.EventRequestRepository;
import ru.practicum.explore.repositories.LocationRepository;
import ru.practicum.explore.services.private_api.PrivateEventService;
import ru.practicum.explore.util.DataFinder;
import ru.practicum.explore.util.UtilMergeProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final EventRequestRepository eventRequestRepository;
    private final LocationRepository locationRepository;
    private final DataFinder dataFinder;

    @Override
    public Collection<EventShortDto> readAllEvents(Long initiatorId, Pageable pageable) {
        Page<Event> events = eventRepository.readAllInitiatorEvents(initiatorId, pageable);

        if (events.getContent().isEmpty()) {
            return new ArrayList<>();
        }

        return events.getContent().stream()
                .map(EventMapper::mapToEventShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto createEvent(Long initiatorId, NewEventDto newEventDto) {
        LocalDateTime eventDate = newEventDto.getEventDate();

        checkEventDate(eventDate);

        User initiator = dataFinder.findUserById(initiatorId);

        Category category = dataFinder.findCategoryById(newEventDto.getCategory());

        Location location = locationRepository.save(newEventDto.getLocation());

        Event event = EventMapper.mapToEvent(newEventDto, category, location, initiator, eventDate,
                EventLifecycle.PENDING, 0L, 0L);

        return EventMapper.mapToEventFullDto(eventRepository.save(event));
    }

    @Override
    public EventFullDto readEvent(Long initiatorId, Long eventId) {
        Event event = dataFinder.findEventById(eventId);

        return EventMapper.mapToEventFullDto(event);
    }

    @Override
    public EventFullDto updateEvent(Long initiatorId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
        Event eventToUpdate = dataFinder.findEventById(eventId);
        LocalDateTime newEventDate = updateEventUserRequest.getEventDate();

        if (newEventDate != null) {
            checkEventDate(newEventDate);
        }

        if (EventLifecycle.valueOf(eventToUpdate.getState()) == EventLifecycle.PUBLISHED) {
            throw new ConditionMismatchException("Only pending or canceled events can be changed");
        }

        if (updateEventUserRequest.getStateAction() != null) {
            if (updateEventUserRequest.getStateAction() == UserEventState.CANCEL_REVIEW) {
                eventToUpdate.setState(EventLifecycle.CANCELED.toString());
            } else {
                eventToUpdate.setState(EventLifecycle.PENDING.toString());
            }
        }

        UtilMergeProperty.copyProperties(updateEventUserRequest, eventToUpdate);

        return EventMapper.mapToEventFullDto(eventRepository.save(eventToUpdate));
    }

    @Override
    public Collection<ParticipationRequestDto> readEventRequests(Long initiatorId, Long eventId) {
        Collection<EventRequest> requests = eventRequestRepository.findAllByEvent_Id(eventId)
                .stream()
                .filter(request -> request.getEvent().getInitiator().getId().equals(initiatorId))
                .collect(Collectors.toList());

        if (requests.isEmpty()) {
            return new ArrayList<>();
        }

        return requests.stream()
                .map(EventRequestMapper::mapToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public EventRequestStatusUpdateResult updateRequestStatus(Long initiatorId, Long eventId,
                                                              EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        Event event = dataFinder.findEventById(eventId);

        Collection<EventRequest> requestsToUpdate = eventRequestStatusUpdateRequest.getRequestIds()
                .stream()
                .map(dataFinder::findEventRequestById)
                .collect(Collectors.toList());

        for (EventRequest request : requestsToUpdate) {
            if (!request.getStatus().equals(RequestStatus.PENDING.toString())) {
                throw new ConditionMismatchException("Request must have status PENDING");
            }
        }

        Collection<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        Collection<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        if (eventRequestStatusUpdateRequest.getStatus() == RequestStatus.REJECTED) {
            rejectedRequests = requestsToUpdate.stream()
                    .map(request -> {
                        request.setStatus(RequestStatus.REJECTED.toString());
                        eventRequestRepository.save(request);
                        return EventRequestMapper.mapToParticipationRequestDto(request);
                    })
                    .collect(Collectors.toList());
        }

        if (eventRequestStatusUpdateRequest.getStatus() == RequestStatus.CONFIRMED) {
            if (event.getParticipantLimit() == 0 || !event.getRequestModeration()) {
                confirmedRequests = requestsToUpdate.stream()
                        .map(request -> {
                            request.setStatus(RequestStatus.CONFIRMED.toString());
                            eventRequestRepository.save(request);
                            return EventRequestMapper.mapToParticipationRequestDto(request);
                        })
                        .collect(Collectors.toList());
            } else if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new ConditionMismatchException("The participant limit has been reached");
            }

            for (EventRequest request : requestsToUpdate) {
                if (event.getConfirmedRequests() < event.getParticipantLimit()) {
                    request.setStatus(RequestStatus.CONFIRMED.toString());
                    confirmedRequests.add(EventRequestMapper.mapToParticipationRequestDto(request));
                } else {
                    request.setStatus(RequestStatus.REJECTED.toString());
                    rejectedRequests.add(EventRequestMapper.mapToParticipationRequestDto(request));
                }
                eventRequestRepository.save(request);
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            }
        }

        eventRepository.save(event);

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private void checkEventDate(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2))) {
            throw new InvalidRequestException("Cannot create the event because event date cannot be earlier " +
                    "than two hours after current moment");
        }
    }
}
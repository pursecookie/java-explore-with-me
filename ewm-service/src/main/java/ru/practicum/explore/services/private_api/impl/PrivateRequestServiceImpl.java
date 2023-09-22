package ru.practicum.explore.services.private_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.event_request.ParticipationRequestDto;
import ru.practicum.explore.enums.EventLifecycle;
import ru.practicum.explore.enums.RequestStatus;
import ru.practicum.explore.exceptions.ConditionMismatchException;
import ru.practicum.explore.mappers.EventRequestMapper;
import ru.practicum.explore.models.Event;
import ru.practicum.explore.models.EventRequest;
import ru.practicum.explore.models.User;
import ru.practicum.explore.repositories.EventRepository;
import ru.practicum.explore.repositories.EventRequestRepository;
import ru.practicum.explore.services.private_api.PrivateRequestService;
import ru.practicum.explore.component.DataFinder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final EventRequestRepository eventRequestRepository;
    private final EventRepository eventRepository;
    private final DataFinder dataFinder;

    @Override
    public Collection<ParticipationRequestDto> readAllRequests(Long requesterId) {
        dataFinder.findUserById(requesterId);

        Collection<EventRequest> requests = eventRequestRepository.findAllByRequester_Id(requesterId);

        if (requests.isEmpty()) {
            return new ArrayList<>();
        }

        return requests.stream()
                .map(EventRequestMapper::mapToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Long requesterId, Long eventId) {
        if (!eventRequestRepository.findUserEventRequestByEvent(requesterId, eventId).isEmpty()) {
            throw new ConditionMismatchException("User's request for the event already exists");
        }

        Event event = dataFinder.findEventById(eventId);

        if (!event.getState().equals(EventLifecycle.PUBLISHED.toString())) {
            throw new ConditionMismatchException("The event must be published");
        }

        if (event.getParticipantLimit() != 0) {
            if (event.getConfirmedRequests() >= event.getParticipantLimit()) {
                throw new ConditionMismatchException("The participant limit has been reached");
            }
        }

        if (requesterId.equals(event.getInitiator().getId())) {
            throw new ConditionMismatchException("User cannot create event request to his own event");
        }

        User requester = dataFinder.findUserById(requesterId);

        EventRequest request = new EventRequest(LocalDateTime.now(),
                event,
                requester,
                RequestStatus.PENDING.toString());

        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(RequestStatus.CONFIRMED.toString());
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return EventRequestMapper.mapToParticipationRequestDto(eventRequestRepository.save(request));
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        EventRequest requestToUpdate = dataFinder.findEventRequestById(requestId);

        requestToUpdate.setStatus(RequestStatus.CANCELED.toString());

        return EventRequestMapper.mapToParticipationRequestDto(eventRequestRepository.save(requestToUpdate));
    }
}
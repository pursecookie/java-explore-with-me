package ru.practicum.explore.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.event_request.ParticipationRequestDto;
import ru.practicum.explore.enums.RequestStatus;
import ru.practicum.explore.models.EventRequest;

@UtilityClass
public class EventRequestMapper {
    public ParticipationRequestDto mapToParticipationRequestDto(EventRequest eventRequest) {
        return new ParticipationRequestDto(eventRequest.getCreated(),
                eventRequest.getEvent().getId(),
                eventRequest.getId(),
                eventRequest.getRequester().getId(),
                RequestStatus.valueOf(eventRequest.getStatus()));
    }
}
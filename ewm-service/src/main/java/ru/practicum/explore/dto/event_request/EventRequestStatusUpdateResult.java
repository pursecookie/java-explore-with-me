package ru.practicum.explore.dto.event_request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private Collection<ParticipationRequestDto> confirmedRequests;

    private Collection<ParticipationRequestDto> rejectedRequests;
}
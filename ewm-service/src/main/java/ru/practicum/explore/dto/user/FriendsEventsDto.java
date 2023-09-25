package ru.practicum.explore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.explore.dto.event.EventShortDto;

import java.util.Collection;

@Data
@AllArgsConstructor
public class FriendsEventsDto {
    private final Long id;

    private String name;

    private Collection<EventShortDto> confirmedEvents;
}
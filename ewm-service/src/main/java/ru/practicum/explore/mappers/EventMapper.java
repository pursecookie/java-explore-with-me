package ru.practicum.explore.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.event.EventFullDto;
import ru.practicum.explore.dto.event.EventShortDto;
import ru.practicum.explore.dto.event.NewEventDto;
import ru.practicum.explore.enums.EventLifecycle;
import ru.practicum.explore.models.Category;
import ru.practicum.explore.models.Event;
import ru.practicum.explore.models.Location;
import ru.practicum.explore.models.User;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public EventShortDto mapToEventShortDto(Event event) {
        return new EventShortDto(event.getAnnotation(),
                CategoryMapper.mapToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate(),
                event.getId(),
                UserMapper.mapToUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }

    public EventFullDto mapToEventFullDto(Event event) {
        return new EventFullDto(event.getAnnotation(),
                CategoryMapper.mapToCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getEventDate(),
                event.getId(),
                UserMapper.mapToUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                EventLifecycle.valueOf(event.getState()),
                event.getTitle(),
                event.getViews());
    }

    public Event mapToEvent(NewEventDto newEventDto, Category category, Location location, User initiator,
                            LocalDateTime eventDate, EventLifecycle state, Long confirmedRequests, Long views) {
        return new Event(newEventDto.getAnnotation(),
                category,
                confirmedRequests,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                eventDate,
                initiator,
                location,
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                LocalDateTime.now(),
                newEventDto.getRequestModeration(),
                state.toString(),
                newEventDto.getTitle(),
                views);
    }
}
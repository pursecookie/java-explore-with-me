package ru.practicum.explore.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.models.*;
import ru.practicum.explore.repositories.*;

@Service
@RequiredArgsConstructor
public class DataFinder {
    private final CategoryRepository categoryRepository;
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventRequestRepository eventRequestRepository;
    private final UserRepository userRepository;

    public Category findCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

    public Compilation findCompilationById(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation with id=" + compId + " was not found"));
    }

    public Event findEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
    }

    public EventRequest findEventRequestById(Long requestId) {
        return eventRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id=" + requestId + " was not found"));
    }

    public User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));
    }
}
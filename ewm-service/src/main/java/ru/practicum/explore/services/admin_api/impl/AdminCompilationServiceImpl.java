package ru.practicum.explore.services.admin_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explore.mappers.CompilationMapper;
import ru.practicum.explore.models.Compilation;
import ru.practicum.explore.models.Event;
import ru.practicum.explore.repositories.CompilationRepository;
import ru.practicum.explore.repositories.EventRepository;
import ru.practicum.explore.services.admin_api.AdminCompilationService;
import ru.practicum.explore.component.DataFinder;
import ru.practicum.explore.util.UtilMergeProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCompilationServiceImpl implements AdminCompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final DataFinder dataFinder;

    @Transactional
    @Override
    public CompilationDto createCompilation(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.mapToCompilation(newCompilationDto);
        compilation.setEvents(findEvents(newCompilationDto.getEvents()));

        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilation));
    }

    @Override
    public void deleteCompilation(Long compId) {
        dataFinder.findCompilationById(compId);
        compilationRepository.deleteById(compId);
    }

    @Transactional
    @Override
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilationToUpdate = dataFinder.findCompilationById(compId);

        UtilMergeProperty.copyProperties(updateCompilationRequest, compilationToUpdate);
        compilationToUpdate.setEvents(findEvents(updateCompilationRequest.getEvents()));

        return CompilationMapper.mapToCompilationDto(compilationRepository.save(compilationToUpdate));
    }

    private Collection<Event> findEvents(List<Long> eventIds) {
        if (eventIds.isEmpty()) {
            return new ArrayList<>();
        }

        return eventRepository.findAllByIdIn(eventIds);
    }
}
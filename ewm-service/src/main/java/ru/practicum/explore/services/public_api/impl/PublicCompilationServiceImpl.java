package ru.practicum.explore.services.public_api.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.mappers.CompilationMapper;
import ru.practicum.explore.models.Compilation;
import ru.practicum.explore.repositories.CompilationRepository;
import ru.practicum.explore.services.public_api.PublicCompilationService;
import ru.practicum.explore.util.DataFinder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository compilationRepository;
    private final DataFinder dataFinder;

    @Override
    public Collection<CompilationDto> readAllCompilations(Boolean pinned, Pageable pageable) {
        Collection<Compilation> compilations;

        if (pinned != null) {
            compilations = compilationRepository.findAllByPinned(pinned, pageable).getContent();
        } else {
            compilations = compilationRepository.findAll(pageable).getContent();
        }

        if (compilations.isEmpty()) {
            return new ArrayList<>();
        }

        return compilations.stream()
                .map(CompilationMapper::mapToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto readCompilation(Long compId) {
        Compilation compilation = dataFinder.findCompilationById(compId);

        return CompilationMapper.mapToCompilationDto(compilation);
    }
}
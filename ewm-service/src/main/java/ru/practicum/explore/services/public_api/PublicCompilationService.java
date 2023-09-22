package ru.practicum.explore.services.public_api;

import org.springframework.data.domain.Pageable;
import ru.practicum.explore.dto.compilation.CompilationDto;

import java.util.Collection;

public interface PublicCompilationService {
    Collection<CompilationDto> readAllCompilations(Boolean pinned, Pageable pageable);

    CompilationDto readCompilation(Long compId);
}
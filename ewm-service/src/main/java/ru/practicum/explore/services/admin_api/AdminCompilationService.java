package ru.practicum.explore.services.admin_api;

import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.dto.compilation.UpdateCompilationRequest;

public interface AdminCompilationService {
    CompilationDto createCompilation(NewCompilationDto newCompilationDto);

    void deleteCompilation(Long compId);

    CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest);
}
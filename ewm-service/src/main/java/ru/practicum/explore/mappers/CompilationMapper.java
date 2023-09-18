package ru.practicum.explore.mappers;

import lombok.experimental.UtilityClass;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.dto.compilation.NewCompilationDto;
import ru.practicum.explore.models.Compilation;

import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {
    public Compilation mapToCompilation(NewCompilationDto newCompilationDto) {
        return new Compilation(newCompilationDto.getTitle(),
                newCompilationDto.getPinned());
    }

    public CompilationDto mapToCompilationDto(Compilation compilation) {
        return new CompilationDto(compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                compilation.getEvents().stream()
                        .map(EventMapper::mapToEventShortDto)
                        .collect(Collectors.toList()));
    }
}
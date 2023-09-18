package ru.practicum.explore.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.dto.event.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private Long id;

    private String title;

    private Boolean pinned;

    private List<EventShortDto> events;
}
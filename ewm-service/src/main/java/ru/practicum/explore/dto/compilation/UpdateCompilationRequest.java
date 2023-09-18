package ru.practicum.explore.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {
    private List<Long> events;

    private Boolean pinned;

    @Size(min = 1)
    @Size(max = 50)
    private String title;
}
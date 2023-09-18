package ru.practicum.explore.dto.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {
    private List<Long> events = new ArrayList<>();

    private Boolean pinned = false;

    @Size(min = 1)
    @Size(max = 50)
    @NotBlank(message = "The compilation title cannot be empty")
    private String title;
}
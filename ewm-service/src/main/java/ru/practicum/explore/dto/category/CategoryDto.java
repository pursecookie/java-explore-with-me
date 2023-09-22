package ru.practicum.explore.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class CategoryDto {
    private final Long id;

    @Size(min = 1)
    @Size(max = 50)
    private String name;
}
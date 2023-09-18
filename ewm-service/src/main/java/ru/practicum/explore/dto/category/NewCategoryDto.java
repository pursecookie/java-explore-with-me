package ru.practicum.explore.dto.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCategoryDto {
    @Size(min = 1)
    @Size(max = 50)
    @NotBlank(message = "The category name cannot be empty")
    private String name;
}
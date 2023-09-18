package ru.practicum.explore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserDto {
    private final Long id;

    private String name;

    private String email;
}
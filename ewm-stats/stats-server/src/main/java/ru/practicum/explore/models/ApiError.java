package ru.practicum.explore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private HttpStatus status;

    private String reason;

    private String message;

    private String timestamp;
}
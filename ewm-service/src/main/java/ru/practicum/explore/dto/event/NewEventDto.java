package ru.practicum.explore.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.models.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @Size(min = 20)
    @Size(max = 2000)
    @NotBlank(message = "The event annotation cannot be empty")
    private String annotation;

    private Long category;

    @Size(min = 20)
    @Size(max = 7000)
    @NotBlank(message = "The event description cannot be empty")
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid = false;

    private Long participantLimit = 0L;

    private Boolean requestModeration = true;

    @Size(min = 3)
    @Size(max = 120)
    @NotBlank(message = "The event title cannot be empty")
    private String title;
}
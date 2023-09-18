package ru.practicum.explore.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explore.enums.AdminEventState;
import ru.practicum.explore.models.Location;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    @Size(min = 20)
    @Size(max = 2000)
    private String annotation;

    private Long category;

    @Size(min = 20)
    @Size(max = 7000)
    private String description;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private AdminEventState stateAction;

    @Size(min = 3)
    @Size(max = 120)
    private String title;
}
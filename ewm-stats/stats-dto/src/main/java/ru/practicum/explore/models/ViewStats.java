package ru.practicum.explore.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViewStats {
    private String app;

    private String uri;

    private long hits;
}
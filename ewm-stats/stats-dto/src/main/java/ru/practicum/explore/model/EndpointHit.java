package ru.practicum.explore.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EndpointHit {
    private long id;
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}

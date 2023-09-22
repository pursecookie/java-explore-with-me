package ru.practicum.explore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.exceptions.InvalidRequestException;
import ru.practicum.explore.models.EndpointHit;
import ru.practicum.explore.models.ViewStats;
import ru.practicum.explore.services.EndpointHitService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class EndpointHitController {
    private final EndpointHitService endpointHitService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHit create(@RequestBody EndpointHit endpointHit) {
        return endpointHitService.create(endpointHit);
    }

    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    public List<ViewStats> readAll(@RequestParam(required = false) String start,
                                   @RequestParam(required = false) String end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(defaultValue = "false") Boolean unique) {
        if (start == null || end == null) {
            throw new InvalidRequestException("The start and end dates of the range cannot be empty");
        }

        return endpointHitService.readAll(start, end, uris, unique);
    }
}
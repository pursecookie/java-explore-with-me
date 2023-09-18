package ru.practicum.explore.controllers.public_api;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explore.dto.compilation.CompilationDto;
import ru.practicum.explore.services.public_api.PublicCompilationService;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final PublicCompilationService publicCompilationService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<CompilationDto> readAllCompilations(@RequestParam(required = false) Boolean pinned,
                                                          @RequestParam(defaultValue = "0") Integer from,
                                                          @RequestParam(defaultValue = "10") Integer size) {

        Pageable pageable = PageRequest.of(from / size, size);

        return publicCompilationService.readAllCompilations(pinned, pageable);
    }

    @GetMapping("/{compId}")
    @ResponseStatus(HttpStatus.OK)
    public CompilationDto readCompilation(@PathVariable Long compId) {
        return publicCompilationService.readCompilation(compId);
    }

}

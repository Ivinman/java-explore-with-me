package ru.practicum.part.public_part.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationOutDto;
import ru.practicum.part.public_part.compilations.service.PublicCompilationService;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final PublicCompilationService compService;

    @GetMapping
    public List<CompilationOutDto> getComp(@RequestParam(defaultValue = "false") Boolean pinned,
                                           @RequestParam(defaultValue = "0") Integer from,
                                           @RequestParam(defaultValue = "10") Integer size) throws Exception {
        return compService.getComp(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationOutDto getCompById(@PathVariable Integer compId) throws Exception {
        return compService.getCompById(compId);
    }
}

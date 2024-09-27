package ru.practicum.part.admin_part.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationOutDto;
import ru.practicum.part.admin_part.compilations.service.CompilationsService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class CompilationController {
    private final CompilationsService compilationsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationOutDto addCompilation(@RequestBody CompilationDto compilationDto) throws Exception {
        return compilationsService.addComp(compilationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{compId}")
    public void deleteComp(@PathVariable Integer compId) throws Exception {
        compilationsService.deleteComp(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationOutDto editComp(@PathVariable Integer compId,
                                      @RequestBody CompilationDto compilationDto) throws Exception {
        return compilationsService.editComp(compId, compilationDto);
    }
}

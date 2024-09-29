package ru.practicum.part.admin_part.compilations;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationOutDto;
import ru.practicum.part.admin_part.compilations.service.AdminCompilationsService;

@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final AdminCompilationsService adminCompilationsService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public CompilationOutDto addCompilation(@RequestBody CompilationDto compilationDto) throws Exception {
        return adminCompilationsService.addComp(compilationDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("{compId}")
    public void deleteComp(@PathVariable Integer compId) throws Exception {
        adminCompilationsService.deleteComp(compId);
    }

    @PatchMapping("/{compId}")
    public CompilationOutDto editComp(@PathVariable Integer compId,
                                      @RequestBody CompilationDto compilationDto) throws Exception {
        return adminCompilationsService.editComp(compId, compilationDto);
    }
}

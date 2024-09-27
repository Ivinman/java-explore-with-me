package ru.practicum.part.admin_part.compilations.service;

import ru.practicum.dto.compilation.CompilationDto;
import ru.practicum.dto.compilation.CompilationOutDto;

public interface CompilationsService {
    CompilationOutDto addComp(CompilationDto compilationDto) throws Exception;

    void deleteComp(Integer compId) throws Exception;

    CompilationOutDto editComp(Integer compId, CompilationDto compilationDto) throws Exception;
}

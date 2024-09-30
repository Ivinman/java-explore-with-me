package ru.practicum.part.public_part.compilations.service;

import ru.practicum.dto.compilation.CompilationOutDto;

import java.util.List;

public interface PublicCompilationService {
    List<CompilationOutDto> getComp(Boolean pinned, Integer from, Integer size) throws Exception;

    CompilationOutDto getCompById(Integer compId) throws Exception;
}

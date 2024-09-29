package ru.practicum.dto.compilation;

import ru.practicum.model.compilation.Compilation;

public class CompilationMapper {
    public static CompilationOutDto toCompOutDto(Compilation compilation) {
        CompilationOutDto compilationOutDto = new CompilationOutDto();
        compilationOutDto.setTitle(compilationOutDto.getTitle());
        compilationOutDto.setPinned(compilation.getPinned());
        return compilationOutDto;
    }
}

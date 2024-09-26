package ru.practicum.dto.compilation;

import lombok.Data;

@Data
public class CompilationDto {
    private Integer[] events;
    private Boolean pinned;
    private String title;
}

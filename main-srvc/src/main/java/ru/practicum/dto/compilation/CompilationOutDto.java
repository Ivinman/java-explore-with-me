package ru.practicum.dto.compilation;

import lombok.Data;
import ru.practicum.dto.event.ShortEventDto;

import java.util.List;

@Data
public class CompilationOutDto {
    private List<ShortEventDto> events;
    private Integer id;
    private Boolean pinned;
    private String title;
}

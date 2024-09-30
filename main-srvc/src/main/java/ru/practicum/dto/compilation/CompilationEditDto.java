package ru.practicum.dto.compilation;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CompilationEditDto {
    private Integer[] events;
    private Boolean pinned;

    @Length(min = 1, max = 50, message = "Title is too long")
    private String title;
}

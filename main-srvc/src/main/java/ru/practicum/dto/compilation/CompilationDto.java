package ru.practicum.dto.compilation;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CompilationDto {
    private Integer[] events;
    private Boolean pinned;

    @NotBlank(message = "Field title is blank")
    @Length(min = 1, max = 50, message = "Title is too long")
    private String title;
}

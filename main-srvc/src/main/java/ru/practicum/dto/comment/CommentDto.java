package ru.practicum.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CommentDto {
    @NotBlank(message = "Text is empty")
    @Length(min = 1, max = 250, message = "Text must be in 1 and 250 symbols")
    private String text;
}

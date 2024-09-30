package ru.practicum.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryDto {
    @NotNull(message = "Field name - no value entered")
    @NotBlank(message = "Field name is blank")
    @Length(min = 1, max = 50, message = "Name is too long")
    private String name;
}

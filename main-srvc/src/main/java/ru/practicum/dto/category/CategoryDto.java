package ru.practicum.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryDto {
    @NotBlank(message = "Field name is blank")
    @Length(min = 1, max = 50, message = "Name is too long")
    private String name;
}

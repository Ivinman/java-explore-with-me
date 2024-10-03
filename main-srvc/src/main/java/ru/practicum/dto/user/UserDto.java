package ru.practicum.dto.user;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class UserDto {
    @NotBlank(message = "Email is empty")
    @Length(min = 6, max = 254, message = "Email length must be in 6 and 254 symbols")
    private String email;

    @NotBlank(message = "Name is empty")
    @Length(min = 2, max = 250, message = "Name length must be in 2 and 250 symbols")
    private String name;
}

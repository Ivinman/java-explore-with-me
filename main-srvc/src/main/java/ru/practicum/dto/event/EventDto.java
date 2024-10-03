package ru.practicum.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EventDto {
    @NotBlank(message = "Annotation is blank")
    @Length(min = 20, max = 2000, message = "Annotation length must be in 20 and 2000 symbols")
    private String annotation;

    @NotNull(message = "Category has no value")
    private Integer category;

    @NotBlank(message = "Description is blank")
    @Length(min = 20, max = 7000, message = "Description length must be in 20 and 7000 symbols")
    private String description;

    @NotNull(message = "Event date has no value")
    private String eventDate;

    @NotNull(message = "Location has no value")
    private LocationDto location;

    private Boolean paid;

    @Min(value = 0, message = "Participant limit can not be negative")
    private Integer participantLimit;

    private Boolean requestModeration;

    @NotBlank(message = "Title is blank")
    @Length(min = 3, max = 120, message = "Title length must be in 3 and 120 symbols")
    private String title;
}

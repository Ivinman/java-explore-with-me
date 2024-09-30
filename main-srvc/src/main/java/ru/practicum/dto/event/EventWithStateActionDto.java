package ru.practicum.dto.event;

import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class EventWithStateActionDto {
    @Length(min = 20, max = 2000, message = "Annotation length must be in 20 and 2000 symbols")
    private String annotation;

    private Integer category;

    @Length(min = 20, max = 7000, message = "Description length must be in 20 and 7000 symbols")
    private String description;

    private String eventDate;

    private LocationDto location;

    private Boolean paid;

    @Min(value = 0, message = "Participant limit can not be negative")
    private Integer participantLimit;

    private Boolean requestModeration;

    @Length(min = 3, max = 120, message = "Title length must be in 3 and 120 symbols")
    private String title;

    private String stateAction;
}

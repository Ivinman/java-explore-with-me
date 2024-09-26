package ru.practicum.dto.event;

import lombok.Data;
import ru.practicum.dto.user.UserWithoutEmailDto;
import ru.practicum.model.category.Category;

import java.sql.Timestamp;

@Data
public class FullEventDto {
    private String annotation;
    private Category category;
    private Integer confirmedRequests;
    private Timestamp createdOn;
    private String description;

    private String eventDate;

    private Integer id;
    private UserWithoutEmailDto initiator;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Timestamp publishedOn;
    private Boolean requestModeration;
    private String state;
    private String title;
    private Integer views;
}

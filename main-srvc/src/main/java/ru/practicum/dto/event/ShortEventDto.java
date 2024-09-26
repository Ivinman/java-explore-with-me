package ru.practicum.dto.event;

import lombok.Data;
import ru.practicum.dto.user.UserWithoutEmailDto;
import ru.practicum.model.category.Category;

import java.sql.Timestamp;

@Data
public class ShortEventDto {
    private String annotation;
    private Category category;
    private Integer confirmedRequests;

    private String eventDate;

    private Integer id;
    private UserWithoutEmailDto initiator;
    private Boolean paid;
    private String title;
    private Integer views;
}

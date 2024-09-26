package ru.practicum.dto.event;

import lombok.Data;

@Data
public class EventDto {
    private String annotation;
    private Integer category;
    private String description;
    private String eventDate;
    private LocationDto location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String title;
}

package ru.practicum.dto.event;

import lombok.Data;

@Data
public class EventWithStateActionDto extends EventDto {
    private String stateAction;
}

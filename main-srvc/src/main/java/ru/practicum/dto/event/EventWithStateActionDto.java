package ru.practicum.dto.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EventWithStateActionDto extends EventDto {
    private String stateAction;
}

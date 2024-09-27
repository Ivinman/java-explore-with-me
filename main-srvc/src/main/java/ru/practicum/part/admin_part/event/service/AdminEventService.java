package ru.practicum.part.admin_part.event.service;

import ru.practicum.dto.event.EventWithStateActionDto;
import ru.practicum.dto.event.FullEventDto;

import java.util.List;

public interface AdminEventService {
    List<FullEventDto> getEvents(List<Integer> userIds, List<String> states, List<Integer> categoryIds,
                                 String start, String end, Integer from, Integer size) throws Exception;

    FullEventDto editEvent(Integer eventId, EventWithStateActionDto eventWithStateActionDto) throws Exception;
}

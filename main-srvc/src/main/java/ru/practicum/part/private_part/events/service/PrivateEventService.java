package ru.practicum.part.private_part.events.service;

import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventWithStateActionDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.request.RequestEditDto;
import ru.practicum.dto.request.RequestResponseDto;
import ru.practicum.dto.request.RequestDto;

import java.util.List;

public interface PrivateEventService {
    FullEventDto addEvent(Integer userId, EventDto eventDto) throws Exception;

    List<ShortEventDto> getEvents(Integer userId, Integer from, Integer size) throws Exception;

    FullEventDto getEventById(Integer userId, Integer eventId) throws Exception;

    FullEventDto editEvent(Integer userId, Integer eventId, EventWithStateActionDto eventDto) throws Exception;

    List<RequestDto> getRequests(Integer userId, Integer eventId) throws Exception;

    RequestResponseDto editRequests(Integer userId, Integer eventId, RequestEditDto requestEditDto) throws Exception;

    void deleteEvent(Integer eventId) throws Exception;
}

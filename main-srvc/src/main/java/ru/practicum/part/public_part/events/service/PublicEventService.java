package ru.practicum.part.public_part.events.service;

import jakarta.servlet.http.HttpServletRequest;
import ru.practicum.dto.comment.CommentOutForEventDto;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.ShortEventDto;

import java.util.List;

public interface PublicEventService {
    List<ShortEventDto> getEvents(String text, List<Integer> categories, Boolean paid,
                                  String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                  String sort, Integer from, Integer size, HttpServletRequest request) throws Exception;

    FullEventDto getEvent(Integer eventId, HttpServletRequest request) throws Exception;

    List<CommentOutForEventDto> getComments(Integer eventId, Integer from, Integer size) throws Exception;
}

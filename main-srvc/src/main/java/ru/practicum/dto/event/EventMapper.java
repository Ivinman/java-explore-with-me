package ru.practicum.dto.event;

import ru.practicum.dto.user.UserMapper;
import ru.practicum.model.event.Event;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    public static Event toEvent(EventDto eventDto) {
        Event event = new Event();
        event.setAnnotation(eventDto.getAnnotation());
        event.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        event.setDescription(eventDto.getDescription());
        event.setEventDate(Timestamp.valueOf(eventDto.getEventDate()));
        event.setPaid(eventDto.getPaid());
        event.setParticipantLimit(eventDto.getParticipantLimit());
        event.setRequestModeration(eventDto.getRequestModeration());
        event.setState("PENDING");
        event.setTitle(eventDto.getTitle());
        return event;
    }

    public static FullEventDto toFullEventDto(Event event) {
        FullEventDto fullEventDto = new FullEventDto();
        fullEventDto.setAnnotation(event.getAnnotation());
        fullEventDto.setCategory(event.getCategory());
        fullEventDto.setConfirmedRequests(event.getConfirmedRequests());
        fullEventDto.setCreatedOn(event.getCreatedOn());
        fullEventDto.setDescription(event.getDescription());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        fullEventDto.setEventDate(event.getEventDate().toLocalDateTime().format(formatter));

        fullEventDto.setId(event.getId());
        fullEventDto.setInitiator(UserMapper.toUserWithoutEmailDto(event.getInitiator()));
        fullEventDto.setLocation(LocationMapper.toLocationDto(event.getLocation()));
        fullEventDto.setPaid(event.getPaid());
        fullEventDto.setParticipantLimit(event.getParticipantLimit());
        fullEventDto.setPublishedOn(event.getPublishedOn());
        fullEventDto.setRequestModeration(event.getRequestModeration());
        fullEventDto.setState(event.getState());
        fullEventDto.setTitle(event.getTitle());
        fullEventDto.setViews(event.getViews());
        return fullEventDto;
    }

    public static ShortEventDto toEventShortDto(Event event) {
        ShortEventDto shortEventDto = new ShortEventDto();
        shortEventDto.setAnnotation(event.getAnnotation());
        shortEventDto.setCategory(event.getCategory());
        shortEventDto.setConfirmedRequests(event.getConfirmedRequests());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        shortEventDto.setEventDate(event.getEventDate().toLocalDateTime().format(formatter));

        shortEventDto.setId(event.getId());
        shortEventDto.setInitiator(UserMapper.toUserWithoutEmailDto(event.getInitiator()));
        shortEventDto.setPaid(event.getPaid());
        shortEventDto.setTitle(event.getTitle());
        shortEventDto.setViews(event.getViews());
        return shortEventDto;
    }
}

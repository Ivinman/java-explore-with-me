package ru.practicum.part.admin_part.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.EventWithStateActionDto;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.enums.EventState;
import ru.practicum.enums.EventStateAction;
import ru.practicum.exception.ValidationException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.storage.category.CategoriesRepository;
import ru.practicum.storage.comment.CommentRepository;
import ru.practicum.storage.event.EventRepository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoriesRepository categoriesRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<FullEventDto> getEvents(List<Integer> userIds, List<String> states, List<Integer> categories,
                          String rangeStart, String rangeEnd, Integer from, Integer size) throws Exception {
        if (userIds == null) {
            userIds = eventRepository.getAllUserIds();
        }
        if (states == null) {
            states = eventRepository.getAllStates();
        }
        if (categories == null) {
            categories = eventRepository.getAllCategories();
        }
        LocalDateTime start = LocalDateTime.now();
        if (rangeStart == null) {
            start = start.minusYears(5);
        }
        LocalDateTime end = LocalDateTime.now();
        if (rangeEnd == null) {
            end = end.plusYears(5);
        }
        List<Event> events = eventRepository.getEvents(userIds, states, categories,
                        Timestamp.valueOf(start), Timestamp.valueOf(end))
                .stream().skip(from).limit(size).toList();
        List<FullEventDto> fullEventDtos = new ArrayList<>();
        for (Event event : events) {
            fullEventDtos.add(EventMapper.toFullEventDto(event));
        }
        return fullEventDtos;
    }

    @Override
    public FullEventDto editEvent(Integer eventId, EventWithStateActionDto eventWithStateActionDto) throws Exception {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        Event event = eventRepository.findById(eventId).get();
        if (eventWithStateActionDto.getStateAction() != null) {
            if (eventWithStateActionDto.getEventDate() != null) {
                if (eventWithStateActionDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT.name())
                        && Duration.between(LocalDateTime.now(),
                        Timestamp.valueOf(eventWithStateActionDto.getEventDate()).toLocalDateTime()).toHours() <= 1) {
                    throw new ForbiddenException("Cannot publish the event because eventDate is too early");
                }
            }

            if (!event.getState().equals(EventState.PENDING.name())
                    && (eventWithStateActionDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT.name())
                    || eventWithStateActionDto.getStateAction().equals(EventStateAction.REJECT_EVENT.name()))) {
                throw new ForbiddenException("Cannot publish the event because it's not in the right state: " + event.getState());
            }

            if (eventWithStateActionDto.getStateAction().equals(EventStateAction.PUBLISH_EVENT.name())) {
                event.setState(EventState.PUBLISHED.name());
            }
            if (eventWithStateActionDto.getStateAction().equals(EventStateAction.REJECT_EVENT.name())) {
                event.setState(EventState.CANCELED.name());
            }
        }

        if (eventWithStateActionDto.getAnnotation() != null) {
            event.setAnnotation(eventWithStateActionDto.getAnnotation());
        }

        if (eventWithStateActionDto.getCategory() != null) {
            event.setCategory(categoriesRepository.findById(eventWithStateActionDto.getCategory()).get());
        }

        if (eventWithStateActionDto.getDescription() != null) {
            event.setDescription(eventWithStateActionDto.getDescription());
        }

        if (eventWithStateActionDto.getEventDate() != null) {
            if (Timestamp.valueOf(eventWithStateActionDto.getEventDate()).before(Timestamp.valueOf(LocalDateTime.now()))) {
                throw new ValidationException("Date is expired");
            }
            event.setEventDate(Timestamp.valueOf(eventWithStateActionDto.getEventDate()));
        }

        if (eventWithStateActionDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventWithStateActionDto.getParticipantLimit());
        }

        if (eventWithStateActionDto.getTitle() != null) {
            event.setTitle(eventWithStateActionDto.getTitle());
        }

        if (eventWithStateActionDto.getPaid() != null) {
            event.setPaid(eventWithStateActionDto.getPaid());
        }

        eventRepository.save(event);
        return EventMapper.toFullEventDto(event);
    }

    @Override
    public void deleteComment(Integer eventId, Integer commId) throws Exception {
        commentRepository.deleteById(commId);
    }
}

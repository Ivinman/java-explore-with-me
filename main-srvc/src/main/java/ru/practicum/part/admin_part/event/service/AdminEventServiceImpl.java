package ru.practicum.part.admin_part.event.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.EventWithStateActionDto;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.storage.category.CategoriesRepository;
import ru.practicum.storage.event.EventRepository;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository eventRepository;
    private final CategoriesRepository categoriesRepository;

    @Override
    public List<FullEventDto> getEvents(List<Integer> userIds, List<String> states, List<Integer> categories,
                          String rangeStart, String rangeEnd, Integer from, Integer size) throws Exception {

        System.out.println(categories);

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
                if (eventWithStateActionDto.getStateAction().equals("PUBLISH_EVENT")
                        && Duration.between(LocalDateTime.now(),
                        Timestamp.valueOf(eventWithStateActionDto.getEventDate()).toLocalDateTime()).toHours() <= 1) {
                    throw new ForbiddenException("Cannot publish the event because eventDate is too early");
                }
            }

            if (event.getState().equals("PUBLISHED") && (eventWithStateActionDto.getStateAction().equals("PUBLISH_EVENT")
                    || eventWithStateActionDto.getStateAction().equals("REJECT_EVENT"))) {
                throw new ForbiddenException("Cannot publish the event because it's not in the right state: PUBLISHED");
            }
            if (event.getState().equals("CANCELED") && (eventWithStateActionDto.getStateAction().equals("PUBLISH_EVENT")
                    || eventWithStateActionDto.getStateAction().equals("REJECT_EVENT"))) {
                throw new ForbiddenException("Cannot publish the event because it's not in the right state: CANCELED");
            }
            if (eventWithStateActionDto.getStateAction().equals("PUBLISH_EVENT")) {
                event.setState("PUBLISHED");
            }
            if (eventWithStateActionDto.getStateAction().equals("REJECT_EVENT")) {
                event.setState("CANCELED");
            }
        }

        if (eventWithStateActionDto.getAnnotation() != null) {
            if (eventWithStateActionDto.getAnnotation().length() < 20) {
                throw new BadRequestException("Annotation is too short");
            }
            if (eventWithStateActionDto.getAnnotation().length() > 2000) {
                throw new BadRequestException("Annotation is too long");
            }
            event.setAnnotation(eventWithStateActionDto.getAnnotation());
        }
        if (eventWithStateActionDto.getCategory() != null) {
            event.setCategory(categoriesRepository.findById(eventWithStateActionDto.getCategory()).get());
        }
        if (eventWithStateActionDto.getDescription() != null) {
            if (eventWithStateActionDto.getDescription().length() < 20) {
                throw new BadRequestException("Description is too short");
            }
            if (eventWithStateActionDto.getDescription().length() > 7000) {
                throw new BadRequestException("Description is too long");
            }
            event.setDescription(eventWithStateActionDto.getDescription());
        }
        if (eventWithStateActionDto.getEventDate() != null) {
            if (Timestamp.valueOf(eventWithStateActionDto.getEventDate()).before(Timestamp.valueOf(LocalDateTime.now()))) {
                throw new BadRequestException("Date is expired");
            }
            event.setEventDate(Timestamp.valueOf(eventWithStateActionDto.getEventDate()));
        }
        //

        if (eventWithStateActionDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventWithStateActionDto.getParticipantLimit());
        }

        if (eventWithStateActionDto.getTitle() != null) {
            if (eventWithStateActionDto.getTitle().length() < 3) {
                throw new BadRequestException("Title is too short");
            }
            if (eventWithStateActionDto.getTitle().length() > 120) {
                throw new BadRequestException("Title is too long");
            }
            event.setTitle(eventWithStateActionDto.getTitle());
        }



        eventRepository.save(event);
        return EventMapper.toFullEventDto(event);
    }
}

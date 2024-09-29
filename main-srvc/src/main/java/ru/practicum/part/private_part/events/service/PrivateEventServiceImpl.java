package ru.practicum.part.private_part.events.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.event.*;
import ru.practicum.dto.request.RequestEditDto;
import ru.practicum.dto.request.RequestResponseDto;
import ru.practicum.enums.EventState;
import ru.practicum.enums.EventStateAction;
import ru.practicum.enums.RequestState;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.Location;
import ru.practicum.model.request.Request;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.storage.request.RequestRepository;
import ru.practicum.storage.category.CategoriesRepository;
import ru.practicum.storage.event.EventRepository;
import ru.practicum.storage.event.LocationRepository;
import ru.practicum.storage.user.UserRepository;
import ru.practicum.model.event.Event;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateEventServiceImpl implements PrivateEventService {
    private final EventRepository eventRepository;
    private final LocationRepository locationRepository;
    private final CategoriesRepository categoriesRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;

    @Override
    public FullEventDto addEvent(Integer userId, EventDto eventDto) throws Exception {
        userValid(userId);
        if (eventDto.getAnnotation() == null
                || eventDto.getAnnotation().isBlank()
                || eventDto.getAnnotation().isEmpty()) {
            throw new BadRequestException("Field annotation filled incorrectly");
        }
        if (eventDto.getCategory() == null) {
            throw new BadRequestException("Field category filled incorrectly");
        }
        if (eventDto.getDescription() == null
                || eventDto.getDescription().isBlank()
                || eventDto.getDescription().isEmpty()) {
            throw new BadRequestException("Field description filled incorrectly");
        }
        if (eventDto.getTitle() == null
                || eventDto.getTitle().isBlank()
                || eventDto.getTitle().isEmpty()) {
            throw new BadRequestException("Field title filled incorrectly");
        }

        annotationLengthValid(eventDto);
        descriptionLengthValid(eventDto);
        titleLengthValid(eventDto);

        if (eventDto.getEventDate() == null
                || eventDto.getEventDate().isBlank()
                || eventDto.getEventDate().isEmpty()) {
            throw new BadRequestException("Field eventDate filled incorrectly");
        }

        if (eventDto.getLocation() == null) {
            throw new BadRequestException("Field location filled incorrectly");
        }

        if (eventDto.getParticipantLimit() == null) {
            eventDto.setParticipantLimit(0);
        }

        if (Duration.between(LocalDateTime.now(),
                Timestamp.valueOf(eventDto.getEventDate()).toLocalDateTime()).toHours() <= 2) {
            throw new BadRequestException("Field eventDate field must contain a date that has not yet arrived " +
                    "or is no earlier than 2 hours later");
        }

        if (eventDto.getParticipantLimit() < 0) {
            throw new BadRequestException("Participant limit can not be negative");
        }

        if (eventDto.getPaid() == null) {
            eventDto.setPaid(false);
        }

        if (eventDto.getRequestModeration() == null) {
            eventDto.setRequestModeration(true);
        }

        Event event = EventMapper.toEvent(eventDto);
        event.setCategory(categoriesRepository.findById(eventDto.getCategory()).get());
        event.setInitiator(userRepository.findById(userId).get());
        event.setConfirmedRequests(0);
        event.setViews(0);

        locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));
        event.setLocation(locationRepository.findFirstByOrderByIdDesc());
        eventRepository.save(event);

        return EventMapper.toFullEventDto(event);
    }

    @Override
    public List<ShortEventDto> getEvents(Integer userId, Integer from, Integer size) throws Exception {
        userValid(userId);

        List<Event> events = eventRepository.findByInitiatorId(userId);
        List<ShortEventDto> shortEventDtos = new ArrayList<>();
        for (Event event : events) {
            shortEventDtos.add(EventMapper.toEventShortDto(event));
        }
        return shortEventDtos.stream().skip(from).limit(size).toList();
    }

    @Override
    public FullEventDto getEventById(Integer userId, Integer eventId) throws Exception {
        userValid(userId);

        if (eventRepository.findById(eventId).isPresent()) {
            return EventMapper.toFullEventDto(eventRepository.findById(eventId).get());
        }
        throw new NotFoundException("Event with id=" + eventId + " was not found");
    }

    @Override
    public FullEventDto editEvent(Integer userId, Integer eventId, EventWithStateActionDto eventDto) throws Exception {
        userValid(userId);

        Event event;
        if (eventRepository.findById(eventId).isPresent()) {
            event = eventRepository.findById(eventId).get();
        } else {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        if (event.getState().equals(EventState.PUBLISHED.name())) {
            throw new ForbiddenException("Event must not be published");
        }

        if (eventDto.getAnnotation() != null) {
            annotationLengthValid(eventDto);
            event.setAnnotation(eventDto.getAnnotation());
        }
        if (eventDto.getCategory() != null) {
            event.setCategory(categoriesRepository.findById(eventDto.getCategory()).get());
        }
        if (eventDto.getDescription() != null) {
            descriptionLengthValid(eventDto);
            event.setDescription(eventDto.getDescription());
        }
        if (eventDto.getEventDate() != null) {
            if (Duration.between(LocalDateTime.now(),
                    Timestamp.valueOf(eventDto.getEventDate()).toLocalDateTime()).toHours() <= 2) {
                throw new BadRequestException("Field eventDate field must contain a date that has not yet arrived " +
                        "or is no earlier than 2 hours later");
            }
            event.setEventDate(Timestamp.valueOf(eventDto.getEventDate()));
        }
        if (eventDto.getLocation() != null) {
            Location location = locationRepository.findByLatAndLon(eventDto.getLocation().getLat(),
                    eventDto.getLocation().getLon());
            if (location == null) {
                locationRepository.save(LocationMapper.toLocation(eventDto.getLocation()));
                event.setLocation(locationRepository.findFirstByOrderByIdDesc());
            } else {
                event.setLocation(location);
            }
        }
        if (eventDto.getPaid() != null) {
            event.setPaid(eventDto.getPaid());
        }
        if (eventDto.getParticipantLimit() != null) {
            if (eventDto.getParticipantLimit() < 0) {
                throw new BadRequestException("Participant limit can not be negative");
            }
            event.setParticipantLimit(eventDto.getParticipantLimit());
        }
        if (eventDto.getRequestModeration() != null) {
            event.setRequestModeration(eventDto.getRequestModeration());
        }

        if (eventDto.getStateAction() == null
                || eventDto.getStateAction().equals(EventStateAction.SEND_TO_REVIEW.name())) {
            event.setState(EventState.PENDING.name());
        } else {
            event.setState(EventState.CANCELED.name());
        }

        if (eventDto.getTitle() != null) {
            titleLengthValid(eventDto);
            event.setTitle(eventDto.getTitle());
        }

        eventRepository.save(event);
        return EventMapper.toFullEventDto(event);
    }

    @Override
    public List<RequestDto> getRequests(Integer userId, Integer eventId) throws Exception {
        userValid(userId);
        if (userId == null || eventId == null) {
            throw new BadRequestException("Fields filled incorrectly");
        }
        List<RequestDto> requestDtos = new ArrayList<>();
        for (Request request :requestRepository.findByEventId(eventId)) {
            requestDtos.add(RequestMapper.toDto(request));
        }
        return requestDtos;
    }

    @Override
    public RequestResponseDto editRequests(Integer userId, Integer eventId, RequestEditDto requestEditDto) throws  Exception {
        userValid(userId);
        if (userId == null || eventId == null) {
            throw new BadRequestException("Fields filled incorrectly");
        }
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        if (eventRepository.findById(eventId).get().getParticipantLimit()
                .equals(eventRepository.findById(eventId).get().getConfirmedRequests())) {
            throw new ConflictException("For the requested operation the conditions are not met.",
                    "The participant limit has been reached");
        }

        String status;
        if (requestEditDto.getStatus().equals(RequestState.CONFIRMED.name())) {
            status = RequestState.CONFIRMED.name();
        } else {
            status = RequestState.REJECTED.name();
        }

        RequestResponseDto requestResponseDto = new RequestResponseDto();
        requestResponseDto.setRejectedRequests(new ArrayList<>());
        requestResponseDto.setConfirmedRequests(new ArrayList<>());
        for (Integer reqId : requestEditDto.getRequestIds()) {
            Request requestFromDb = requestRepository.findById(reqId).get();
            if (!requestFromDb.getStatus().equals(RequestState.PENDING.name())) {
                throw new ConflictException("For the requested operation the conditions are not met.",
                        "Request status is not pending");
            }

            Event eventFromDb = eventRepository.findById(eventId).get();
            if (eventFromDb.getParticipantLimit()
                    .equals(eventFromDb.getConfirmedRequests())
                    && eventFromDb.getParticipantLimit() != 0) {
                requestResponseDto.getRejectedRequests().add(RequestMapper.toDto(requestFromDb));
                requestFromDb.setStatus("REJECTED");
                requestRepository.save(requestFromDb);
                continue;
            }

            Request request = requestRepository.findById(reqId).get();
            if (status.equals(RequestState.CONFIRMED.name())) {
                request.setStatus(RequestState.CONFIRMED.name());
                requestResponseDto.getConfirmedRequests().add(RequestMapper.toDto(request));

                Integer confReq = eventRepository.findById(eventId).get().getConfirmedRequests();
                eventRepository.findById(eventId).get().setConfirmedRequests(confReq + 1);
                eventRepository.save(eventRepository.findById(eventId).get());
            } else {
                request.setStatus(RequestState.REJECTED.name());
                requestResponseDto.getRejectedRequests().add(RequestMapper.toDto(request));
            }
            request.setStatus(status);
            requestRepository.save(request);
        }
        return requestResponseDto;
    }

    public void deleteEvent(Integer eventId) throws Exception {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " not found");
        }
        eventRepository.deleteById(eventId);
    }

    private void userValid(Integer userId) throws Exception {
        if (userRepository.findById(userId).isEmpty()) {
            throw new BadRequestException("User is not authorized");
        }
    }

    private void annotationLengthValid(EventDto eventDto) throws Exception {
        if (eventDto.getAnnotation().length() < 20) {
            throw new BadRequestException("Annotation is too short");
        }
        if (eventDto.getAnnotation().length() > 2000) {
            throw new BadRequestException("Annotation is too long");
        }
    }

    private void descriptionLengthValid(EventDto eventDto) throws Exception {
        if (eventDto.getDescription().length() < 20) {
            throw new BadRequestException("Description is too short");
        }
        if (eventDto.getDescription().length() > 7000) {
            throw new BadRequestException("Description is too long");
        }
    }

    private void titleLengthValid(EventDto eventDto) throws Exception {
        if (eventDto.getTitle().length() < 3) {
            throw new BadRequestException("Title is too short");
        }
        if (eventDto.getTitle().length() > 120) {
            throw new BadRequestException("Title is too long");
        }
    }
}

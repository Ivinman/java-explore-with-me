package ru.practicum.part.public_part.events.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.HitClient;
import ru.practicum.HitStatDto;
import ru.practicum.dto.comment.CommentMapper;
import ru.practicum.dto.comment.CommentOutForEventDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.enums.EventState;
import ru.practicum.exception.ValidationException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.event.Event;
import ru.practicum.storage.comment.CommentRepository;
import ru.practicum.storage.event.EventRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final HitClient hitClient;
    private final CommentRepository commentRepository;

    @Override
    public List<ShortEventDto> getEvents(String text, List<Integer> categories, Boolean paid,
                                         String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                         String sort, Integer from, Integer size, HttpServletRequest request) throws Exception {
        if (text == null) {
            text = " ";
        }
        if (categories == null || categories.isEmpty()) {
            categories = eventRepository.getAllCategories();
        }
        if (categories.contains(0)) {
            throw new ValidationException("Category with id=0 is not exist");
        }
        LocalDateTime start;
        if (rangeStart == null) {
            start = LocalDateTime.now().minusYears(5);
        } else {
            start = Timestamp.valueOf(rangeStart).toLocalDateTime();
        }
        LocalDateTime end;
        if (rangeEnd == null) {
            end = LocalDateTime.now().plusYears(5);
        } else {
            end = Timestamp.valueOf(rangeEnd).toLocalDateTime();
        }

        if (sort.equals("EVENT_DATE")) {
            sort = "eventDate";
        }
        if (sort.equals("VIEWS")) {
            sort = "views";
        }

        List<Event> events = new ArrayList<>(eventRepository.getPublicEventsWithText(categories, paid,
                        Timestamp.valueOf(start), Timestamp.valueOf(end), EventState.PUBLISHED.name(), text,
                        Sort.by(Sort.Direction.ASC, sort))
                .stream().skip(from).limit(size).toList());
        if (onlyAvailable) {
            events.removeIf(event -> event.getParticipantLimit() == 0);
        }
        List<ShortEventDto> shortEventDtos = new ArrayList<>();
        HitDto hitDto = getHitDto(request);
        for (Event event : events) {
            shortEventDtos.add(EventMapper.toEventShortDto(event));
        }
        return shortEventDtos;
    }

    @Override
    public FullEventDto getEvent(Integer eventId, HttpServletRequest request) throws Exception {
        Optional<Event> eventFromDb = eventRepository.findById(eventId);
        eventCheck(eventFromDb, eventId);
        Event event = eventFromDb.get();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        HitDto hitDto = getHitDto(request);
        List<HitStatDto> hitStatDtoList = hitClient.getStats(LocalDateTime.now().minusYears(5).format(formatter),
                        LocalDateTime.now().plusYears(5).format(formatter), List.of(hitDto.getUri()), true);
        Integer views = hitStatDtoList.getFirst().getHits();

        event.setViews(views);
        eventRepository.save(event);

        return EventMapper.toFullEventDto(event);
    }

    @Override
    public List<CommentOutForEventDto> getComments(Integer eventId, Integer from, Integer size) throws Exception {
        eventCheck(eventRepository.findById(eventId), eventId);
        List<CommentOutForEventDto> commentOutDtoList = new ArrayList<>();
        for (Comment comment: commentRepository.findByEventId(eventId)) {
            commentOutDtoList.add(CommentMapper.toCommOutForEvent(comment));
        }
        return commentOutDtoList.stream().skip(from).limit(size).toList();
    }

    private void eventCheck(Optional<Event> eventFromDb, Integer eventId) throws Exception {
        if (eventFromDb.isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        if (!eventFromDb.get().getState().equals(EventState.PUBLISHED.name())) {
            throw new NotFoundException("Event with id=" + eventId + " is not published");
        }
    }

    private HitDto getHitDto(HttpServletRequest request) {
        HitDto hitDto = new HitDto();
        hitDto.setApp("main-srvc");
        hitDto.setUri(request.getRequestURI());
        hitDto.setIp(request.getRemoteAddr());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        hitDto.setTimestamp(LocalDateTime.now().format(formatter));

        hitClient.addHit(hitDto);
        return hitDto;
    }
}

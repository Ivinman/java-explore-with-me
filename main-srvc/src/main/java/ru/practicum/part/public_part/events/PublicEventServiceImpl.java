package ru.practicum.part.public_part.events;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.HitDto;
import ru.practicum.HitClient;
import ru.practicum.HitStatDto;
import ru.practicum.dto.event.EventMapper;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.storage.event.EventRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository eventRepository;
    private final HitClient hitClient;

    @Override
    public List<ShortEventDto> getEvents(String text, List<Integer> categories, Boolean paid,
                                         String rangeStart, String rangeEnd, Boolean onlyAvailable,
                                         String sort, Integer from, Integer size, HttpServletRequest request) throws Exception {
        if (text == null) {
            text = " ";
        }
        if (categories.isEmpty()) {
            categories = eventRepository.getAllCategories();
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

        List<Event> events = new ArrayList<>(eventRepository.getPublicEventsWithText(categories, paid,
                        Timestamp.valueOf(start), Timestamp.valueOf(end), "PUBLISHED", text,
                        Sort.by(Sort.Direction.ASC, sort))
                .stream().skip(from).limit(size).toList());
        if (onlyAvailable) {
            events.removeIf(event -> event.getParticipantLimit() == 0);
        }
        List<ShortEventDto> shortEventDtos = new ArrayList<>();

        HitDto hitDto = new HitDto();
        hitDto.setApp("main-srvc");
        hitDto.setUri(request.getRequestURI());
        hitDto.setIp(request.getRemoteAddr());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        hitDto.setTimestamp(LocalDateTime.now().format(formatter));

        for (Event event : events) {
            shortEventDtos.add(EventMapper.toEventShortDto(event));

            hitClient.addHit(hitDto);
             List<HitStatDto> hitStatDtoList = (List<HitStatDto>) hitClient.getStats(LocalDateTime.now().minusYears(5).format(formatter),
                            LocalDateTime.now().plusYears(5).format(formatter), List.of(hitDto.getUri()), true)
                    .getBody();
             Integer views = hitStatDtoList.getFirst().getHits();


            event.setViews(views);
            eventRepository.save(event);
        }
        return shortEventDtos;
    }

    @Override
    public FullEventDto getEvent(Integer eventId, HttpServletRequest request) throws Exception {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        if (!eventRepository.findById(eventId).get().getState().equals("PUBLISHED")) {
            throw new NotFoundException("Event with id=" + eventId + " is not published");
        }
        Event event = eventRepository.findById(eventId).get();

        HitDto hitDto = new HitDto();
        hitDto.setApp("main-srvc");
        hitDto.setUri(request.getRequestURI());
        hitDto.setIp(request.getRemoteAddr());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        hitDto.setTimestamp(LocalDateTime.now().format(formatter));

        hitClient.addHit(hitDto);
        List<HitStatDto> hitStatDtoList = (List<HitStatDto>) hitClient.getStats(LocalDateTime.now().minusYears(5).format(formatter),
                        LocalDateTime.now().plusYears(5).format(formatter), List.of(hitDto.getUri()), true);
        Integer views = hitStatDtoList.getFirst().getHits();

        event.setViews(views);
        eventRepository.save(event);

        return EventMapper.toFullEventDto(event);
    }
}

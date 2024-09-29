package ru.practicum.part.private_part.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventWithStateActionDto;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.part.private_part.events.service.PrivateEventService;
import ru.practicum.dto.request.RequestEditDto;
import ru.practicum.dto.request.RequestResponseDto;
import ru.practicum.dto.request.RequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventsController {
    private final PrivateEventService privateEventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public FullEventDto addEvent(@PathVariable Integer userId,
                                 @RequestBody EventDto eventDto) throws Exception {
        return privateEventService.addEvent(userId, eventDto);
    }

    @GetMapping
    public List<ShortEventDto> getEvents(@PathVariable Integer userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) throws Exception {
        return privateEventService.getEvents(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public FullEventDto getEventById(@PathVariable Integer userId,
                                     @PathVariable Integer eventId) throws Exception {
        return privateEventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{eventId}")
    public FullEventDto editEvent(@PathVariable Integer userId,
                                  @PathVariable Integer eventId,
                                  @RequestBody EventWithStateActionDto eventDto) throws Exception {
        return privateEventService.editEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestDto> getRequests(@PathVariable Integer userId,
                                        @PathVariable Integer eventId) throws Exception {
        return privateEventService.getRequests(userId, eventId);
    }

    @PatchMapping("{eventId}/requests")
    public RequestResponseDto editRequests(@PathVariable Integer userId,
                                           @PathVariable Integer eventId,
                                           @RequestBody RequestEditDto requestEditDto) throws Exception {
        return privateEventService.editRequests(userId, eventId, requestEditDto);
    }


    @DeleteMapping("/{eventId}")
    public void deleteEvent(@PathVariable Integer eventId) throws Exception {
        privateEventService.deleteEvent(eventId);
    }
}

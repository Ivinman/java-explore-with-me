package ru.practicum.part.private_part.events;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventDto;
import ru.practicum.dto.event.EventWithStateActionDto;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.dto.event.ShortEventDto;
import ru.practicum.model.event.Event;
import ru.practicum.part.private_part.events.service.PrivateEventService;
import ru.practicum.part.private_part.events.service.RequestEditDto;
import ru.practicum.part.private_part.events.service.RequestResponse;
import ru.practicum.part.private_part.requests.Request;
import ru.practicum.part.private_part.requests.RequestDto;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class PrivateEventsController {
    private final PrivateEventService privateEventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public FullEventDto addEvent(@PathVariable Integer userId,
                                 @RequestBody EventDto eventDto) throws Exception {
        return privateEventService.addEvent(userId, eventDto);
    }

    @GetMapping("/{userId}/events")
    public List<ShortEventDto> getEvents(@PathVariable Integer userId,
                                         @RequestParam(defaultValue = "0") Integer from,
                                         @RequestParam(defaultValue = "10") Integer size) throws Exception {
        return privateEventService.getEvents(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public FullEventDto getEventById(@PathVariable Integer userId,
                                     @PathVariable Integer eventId) throws Exception {
        return privateEventService.getEventById(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public FullEventDto editEvent(@PathVariable Integer userId,
                                  @PathVariable Integer eventId,
                                  @RequestBody EventWithStateActionDto eventDto) throws Exception {
        return privateEventService.editEvent(userId, eventId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<RequestDto> getRequests(@PathVariable Integer userId,
                                        @PathVariable Integer eventId) throws Exception {
        return privateEventService.getRequests(userId, eventId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public RequestResponse editRequests(@PathVariable Integer userId,
                                        @PathVariable Integer eventId,
                                        @RequestBody RequestEditDto requestEditDto) throws Exception {
        return privateEventService.editRequests(userId, eventId, requestEditDto);
    }


    @DeleteMapping("/{userId}/events/{eventId}")
    public void deleteEvent(@PathVariable Integer eventId) {
        privateEventService.deleteEvent(eventId);
    }
}

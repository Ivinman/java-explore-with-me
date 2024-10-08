package ru.practicum.part.admin_part.event;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.event.EventWithStateActionDto;
import ru.practicum.dto.event.FullEventDto;
import ru.practicum.part.admin_part.event.service.AdminEventService;

import java.util.List;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    public List<FullEventDto> getEvent(@RequestParam(required = false) List<Integer> userId,
                                       @RequestParam(required = false) List<String> states,
                                       @RequestParam(required = false) List<Integer> categories,
                                       @RequestParam(required = false) String start,
                                       @RequestParam(required = false) String end,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) throws Exception {
        return adminEventService.getEvents(userId, states, categories, start, end, from, size);
    }

    @PatchMapping("/{eventId}")
    public FullEventDto editEvent(@PathVariable Integer eventId,
                           @RequestBody @Valid EventWithStateActionDto eventWithStateActionDto) throws Exception {
        return adminEventService.editEvent(eventId, eventWithStateActionDto);
    }

    @DeleteMapping("/{eventId}/comments/{commId}")
    public void deleteComment(@PathVariable Integer eventId, @PathVariable Integer commId) throws Exception {
        adminEventService.deleteComment(eventId, commId);
    }
}

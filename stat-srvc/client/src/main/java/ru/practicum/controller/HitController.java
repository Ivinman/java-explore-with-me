package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.client.HitClient;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class HitController {
    private final HitClient hitClient;

    @PostMapping("/hit")
    public ResponseEntity<Object> addHit(@RequestBody HitDto hitDto) {
        return hitClient.addHit(hitDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> getStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam(required = false) List<String> uris,
                                           @RequestParam(defaultValue = "false") Boolean unique) {
        return hitClient.getStats(start, end, uris, unique);
    }
}

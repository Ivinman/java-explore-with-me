package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitDto;
import ru.practicum.service.HitService;
import ru.practicum.HitStatDto;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class HitController {
    private final HitService hitServiceImpl;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/hit")
    public void addHit(@RequestBody HitDto hitDto) {
        hitServiceImpl.addHit(hitDto);
    }

    @GetMapping("/stats")
    public List<HitStatDto> getStats(@RequestParam String start,
                                     @RequestParam String end,
                                     @RequestParam(required = false) List<String> uris,
                                     @RequestParam(defaultValue = "false") Boolean unique) throws Exception {
        return hitServiceImpl.getStats(start, end, uris, unique);
    }
}

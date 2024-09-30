package ru.practicum.service;

import ru.practicum.HitDto;
import ru.practicum.HitStatDto;

import java.util.List;

public interface HitService {
    void addHit(HitDto hitDto);

    List<HitStatDto> getStats(String start, String end, List<String> uris, Boolean unique) throws Exception;
}

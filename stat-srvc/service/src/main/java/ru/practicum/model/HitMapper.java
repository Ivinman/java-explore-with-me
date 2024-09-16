package ru.practicum.model;

import ru.practicum.HitDto;
import ru.practicum.HitStatDto;

import java.sql.Timestamp;

public class HitMapper {
    public static Hit toHit(HitDto hitDto) {
        Hit hit = new Hit();
        hit.setApp(hitDto.getApp());
        hit.setUri(hitDto.getUri());
        hit.setIp(hitDto.getIp());
        hit.setTimestamp(Timestamp.valueOf(hitDto.getTimestamp()));
        return hit;
    }

    public static HitStatDto toHitStatDto(Hit hit) {
        HitStatDto hitStatDto = new HitStatDto();
        hitStatDto.setApp(hit.getApp());
        hitStatDto.setUri(hit.getUri());
        return hitStatDto;
    }
}

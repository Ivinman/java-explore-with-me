package ru.practicum;

import lombok.Data;

@Data
public class HitStatDto {
    private String app;
    private String uri;
    private Integer hits;
}

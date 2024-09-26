package ru.practicum.part.private_part.events.service;

import lombok.Data;

@Data
public class RequestEditDto {
    private Integer[] requestIds;
    private String status;
}

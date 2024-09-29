package ru.practicum.dto.request;

import lombok.Data;

@Data
public class RequestEditDto {
    private Integer[] requestIds;
    private String status;
}

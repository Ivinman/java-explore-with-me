package ru.practicum.dto.request;

import lombok.Data;

@Data
public class RequestDto {
    private String created;
    private Integer event;
    private Integer id;
    private Integer requester;
    private String status;
}

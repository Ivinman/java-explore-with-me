package ru.practicum.part.private_part.requests;

import lombok.Data;

@Data
public class RequestDto {
    private String created;
    private Integer event;
    private Integer id;
    private Integer requester;
    private String status;
}

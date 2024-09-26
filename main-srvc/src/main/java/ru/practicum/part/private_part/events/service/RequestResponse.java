package ru.practicum.part.private_part.events.service;

import lombok.Data;
import ru.practicum.part.private_part.requests.Request;
import ru.practicum.part.private_part.requests.RequestDto;

import java.util.List;

@Data
public class RequestResponse {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}

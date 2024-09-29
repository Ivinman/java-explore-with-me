package ru.practicum.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestResponseDto {
    private List<RequestDto> confirmedRequests;
    private List<RequestDto> rejectedRequests;
}

package ru.practicum.part.private_part.requests;

import java.time.format.DateTimeFormatter;

public class RequestMapper {
    public static RequestDto toDto (Request request) {
        RequestDto requestDto = new RequestDto();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        requestDto.setCreated(request.getCreated().toLocalDateTime().format(formatter));

        requestDto.setEvent(request.getEvent().getId());
        requestDto.setId(request.getId());
        requestDto.setRequester(request.getRequester().getId());
        requestDto.setStatus(request.getStatus());
        return requestDto;
    }
}

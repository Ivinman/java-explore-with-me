package ru.practicum.part.private_part.requests.service;

import ru.practicum.dto.request.RequestDto;

import java.util.List;

public interface PrivateRequestService {
    RequestDto addRequest(Integer userId, Integer eventId) throws Exception;

    List<RequestDto> getRequests(Integer userId) throws Exception;

    RequestDto cancelRequest(Integer userId, Integer requestId) throws Exception;
}

package ru.practicum.part.private_part.requests.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.enums.EventState;
import ru.practicum.enums.RequestState;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.model.request.Request;
import ru.practicum.dto.request.RequestDto;
import ru.practicum.dto.request.RequestMapper;
import ru.practicum.storage.request.RequestRepository;
import ru.practicum.storage.event.EventRepository;
import ru.practicum.storage.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public RequestDto addRequest(Integer userId, Integer eventId) throws Exception {
        userValid(userId);
        Optional<Event> event = eventRepository.findById(eventId);
        if (event.isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId) != null) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Request already exist");
        }
        if (event.get().getInitiator().getId().equals(userId)) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Initiator can not be requester");
        }
        if (!event.get().getState().equals(EventState.PUBLISHED.name())) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Event must be published");
        }
        if (event.get().getParticipantLimit()
                .equals(event.get().getConfirmedRequests()) && event.get().getParticipantLimit() != 0) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Participant limit is reached");
        }

        Request request = new Request();
        request.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        request.setEvent(event.get());
        request.setRequester(userRepository.findById(userId).get());

        if (event.get().getParticipantLimit() == 0
                || !event.get().getRequestModeration()) {
            Integer confReq = event.get().getConfirmedRequests();
            event.get().setConfirmedRequests(confReq + 1);
            eventRepository.save(event.get());
            request.setStatus(RequestState.CONFIRMED.name());
        } else {
            request.setStatus(RequestState.PENDING.name());
        }

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getRequests(Integer userId) throws Exception {
        userValid(userId);

        List<RequestDto> requestDtos = new ArrayList<>();
        for (Request request : requestRepository.findByRequesterId(userId)) {
            requestDtos.add(RequestMapper.toDto(request));
        }
        return requestDtos;
    }

    @Override
    public RequestDto cancelRequest(Integer userId, Integer requestId) throws Exception {
        userValid(userId);

        Request request = requestRepository.findById(requestId).get();
        request.setStatus(RequestState.CANCELED.name());
        return RequestMapper.toDto(requestRepository.save(request));
    }

    private void userValid(Integer userId) throws Exception {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
    }
}

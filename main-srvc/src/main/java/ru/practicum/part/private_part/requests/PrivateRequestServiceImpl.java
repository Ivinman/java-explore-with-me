package ru.practicum.part.private_part.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.storage.event.EventRepository;
import ru.practicum.storage.user.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PrivateRequestServiceImpl implements PrivateRequestService {
    private final RequestRepository requestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public RequestDto addRequest(Integer userId, Integer eventId) throws Exception {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId) != null) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Request already exist");
        }
        if (eventRepository.findById(eventId).get().getInitiator().getId().equals(userId)) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Initiator can not be requester");
        }
        if (!eventRepository.findById(eventId).get().getState().equals("PUBLISHED")) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Event must be published");
        }
        if (eventRepository.findById(eventId).get().getParticipantLimit()
                .equals(eventRepository.findById(eventId).get().getConfirmedRequests())
                && eventRepository.findById(eventId).get().getParticipantLimit() != 0) {
            throw new ConflictException("Integrity constraint has been violated.",
                    "Participant limit is reached");
        }



        Request request = new Request();
        request.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        request.setEvent(eventRepository.findById(eventId).get());
        request.setRequester(userRepository.findById(userId).get());

        if (eventRepository.findById(eventId).get().getParticipantLimit() == 0
                || !eventRepository.findById(eventId).get().getRequestModeration()) {
            Integer confReq = eventRepository.findById(eventId).get().getConfirmedRequests();
            eventRepository.findById(eventId).get().setConfirmedRequests(confReq + 1);
            eventRepository.save(eventRepository.findById(eventId).get());
            request.setStatus("CONFIRMED");
        } else {
            request.setStatus("PENDING");
        }

        return RequestMapper.toDto(requestRepository.save(request));
    }

    @Override
    public List<RequestDto> getRequests(Integer userId) throws Exception {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
        try {
            List<RequestDto> requestDtos = new ArrayList<>();
            for (Request request : requestRepository.findByRequesterId(userId)) {
                requestDtos.add(RequestMapper.toDto(request));
            }
            return requestDtos;
        } catch (Exception e) {
            throw new BadRequestException("Fields filled incorrectly");
        }
    }

    @Override
    public RequestDto cancelRequest(Integer userId, Integer requestId) throws Exception {
        Request request = requestRepository.findById(requestId).get();
        request.setStatus("CANCELED");
        return RequestMapper.toDto(requestRepository.save(request));
    }
}

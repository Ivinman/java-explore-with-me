package ru.practicum.part.private_part.requests;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateRequestController {
    private final PrivateRequestService requestService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public RequestDto addRequest(@PathVariable Integer userId,
                              @RequestParam Integer eventId) throws Exception {
        return requestService.addRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getRequests(@PathVariable Integer userId) throws Exception {
        return requestService.getRequests(userId);
    }

    @PatchMapping("{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Integer userId,
                                 @PathVariable Integer requestId) throws Exception {
        return requestService.cancelRequest(userId, requestId);
    }
}

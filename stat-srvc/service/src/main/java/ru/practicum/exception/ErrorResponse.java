package ru.practicum.exception;

import lombok.Data;

import java.util.Map;

@Data
public class ErrorResponse {
    private final String status;
    private final String reason;
    private final String message;
    private final String timestamp;

    public ErrorResponse(Map<String, String> error) {
        status = error.get("status");
        reason = error.get("reason");
        message = error.get("message");
        timestamp = error.get("timestamp");
    }
}

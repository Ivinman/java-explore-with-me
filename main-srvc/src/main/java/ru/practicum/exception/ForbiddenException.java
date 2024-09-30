package ru.practicum.exception;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class ForbiddenException extends Exception {
    public ForbiddenException(final String message) {
        super(message);
    }

    public Map<String, String> getErrorMessage() {
        return Map.of(
                "status", "FORBIDDEN",
                "reason", "For the requested operation the conditions are not met.",
                "message", getMessage(),
                "timestamp", Timestamp.valueOf(LocalDateTime.now()).toString());
    }
}

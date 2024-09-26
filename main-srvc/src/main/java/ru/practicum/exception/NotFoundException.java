package ru.practicum.exception;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class NotFoundException extends Exception {
    public NotFoundException(final String message) {
        super(message);
    }

    public Map<String, String> getErrorMessage() {
        return Map.of(
                "status", "NOT_FOUND",
                "reason", "The required object was not found.",
                "message", getMessage(),
                "timestamp", Timestamp.valueOf(LocalDateTime.now()).toString());
    }
}

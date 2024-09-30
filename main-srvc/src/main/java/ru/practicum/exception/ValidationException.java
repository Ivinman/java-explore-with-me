package ru.practicum.exception;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class ValidationException extends Exception {
    public ValidationException(final String message) {
        super(message);
    }

    public Map<String, String> getErrorMessage() {
        return Map.of(
                "status", "BAD_REQUEST",
                "reason", "Incorrectly made request.",
                "message", getMessage(),
                "timestamp", Timestamp.valueOf(LocalDateTime.now()).toString());
    }
}

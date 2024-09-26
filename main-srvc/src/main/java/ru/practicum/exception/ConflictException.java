package ru.practicum.exception;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

public class ConflictException extends Exception {
    private final String reason;

    public ConflictException(String reason, final String message) {
        super(message);
        this.reason = reason;
    }

    public Map<String, String> getErrorMessage() {
        return Map.of(
                "status", "CONFLICT",
                "reason", reason,
                "message", getMessage(),
                "timestamp", Timestamp.valueOf(LocalDateTime.now()).toString());
    }
}

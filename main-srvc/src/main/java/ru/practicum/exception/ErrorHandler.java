package ru.practicum.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMismat(final MethodArgumentTypeMismatchException e) {
        return new ErrorResponse(Map.of(
                "status", HttpStatus.BAD_REQUEST.toString(),
                "reason", "Incorrectly made request.",
                "message", e.getMessage(),
                "timestamp", Timestamp.valueOf(LocalDateTime.now()).toString()
        ));
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestExcep(final BadRequestException e) {
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleConflictExcep(final ConflictException e) {
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleForbiddenExcep(final ForbiddenException e) {
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundExcep(final NotFoundException e) {
        return new ErrorResponse(e.getErrorMessage());
    }
}

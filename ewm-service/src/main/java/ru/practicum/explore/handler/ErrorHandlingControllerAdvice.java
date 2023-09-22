package ru.practicum.explore.handler;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.explore.dto.ApiError;
import ru.practicum.explore.exceptions.ConditionMismatchException;
import ru.practicum.explore.exceptions.InvalidRequestException;
import ru.practicum.explore.exceptions.NotFoundException;
import ru.practicum.explore.exceptions.ValidationException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ErrorHandlingControllerAdvice {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request",
                errors,
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        return new ApiError(HttpStatus.NOT_FOUND,
                "The required object was not found",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(ConditionMismatchException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConditionMismatchException(ConditionMismatchException e) {
        return new ApiError(HttpStatus.CONFLICT,
                "For the requested operation the conditions are not met",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleValidationException(ValidationException e) {
        return new ApiError(HttpStatus.FORBIDDEN,
                "For the requested operation the conditions are not met",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(InvalidRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleInvalidRequestException(InvalidRequestException e) {
        return new ApiError(HttpStatus.BAD_REQUEST,
                "Incorrectly made request",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        return new ApiError(HttpStatus.CONFLICT,
                "Integrity constraint has been violated",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAnyException(Throwable e) {
        return new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                e.getMessage(),
                LocalDateTime.now().format(formatter));
    }
}
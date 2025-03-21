package ru.yandex.practicum.filmorate.exception;


import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ValidationHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ValidationErrorResponse handleOnConstraintValidationException(
            ConstraintViolationException e) {
        final List<ErrorResponse> errorResponses = e.getConstraintViolations().stream()
                .map(error -> new ErrorResponse(
                                error.getPropertyPath().toString(),
                                error.getMessage()
                        )
                )
                .toList();

        log.error(e.getMessage());

        return new ValidationErrorResponse(errorResponses);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler
    public ValidationErrorResponse handleOnMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        final List<ErrorResponse> errorResponses = e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        log.error(e.getMessage());

        return new ValidationErrorResponse(errorResponses);
    }
}

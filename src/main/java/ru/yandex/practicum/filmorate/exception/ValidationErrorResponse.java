package ru.yandex.practicum.filmorate.exception;

import java.util.List;

public record ValidationErrorResponse(List<ErrorResponse> validationErrorList) {
}
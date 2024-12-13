package ru.yandex.practicum.filmorate.model;

import java.time.Duration;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class Film {
    long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotNull
    LocalDate releaseDate;
    @NotNull @PositiveOrZero
    Duration duration;
}

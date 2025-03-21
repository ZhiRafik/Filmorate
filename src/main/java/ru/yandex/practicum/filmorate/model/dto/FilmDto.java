package ru.yandex.practicum.filmorate.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import ru.yandex.practicum.filmorate.validation.group.ValidationGroup;
import ru.yandex.practicum.filmorate.validation.validator.DateAfter;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FilmDto {

    @Null(groups = ValidationGroup.OnCreate.class)
    @NotNull(groups = ValidationGroup.OnUpdate.class)
    @Positive
    Long id;

    @NotBlank(groups = ValidationGroup.OnCreate.class)
    String name;

    @Size(max = 200)
    String description;

    @DateAfter(value = "1895-12-28")
    LocalDate releaseDate;

    @Positive
    Long duration;

    List<CatalogDto> genres;

    @Valid
    @NotNull
    CatalogDto mpa;

    List<CatalogDto> directors;
}

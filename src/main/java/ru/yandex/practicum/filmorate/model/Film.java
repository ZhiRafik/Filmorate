package ru.yandex.practicum.filmorate.model;

import java.time.Duration;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Film {
    long id;
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;
}

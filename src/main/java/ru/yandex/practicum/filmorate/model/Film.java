package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.enums.Genre;
import ru.yandex.practicum.filmorate.enums.MPA;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Set;

@Data
public class Film {
    long id;
    String name;
    String description;
    LocalDate releaseDate;
    Duration duration;
    Set<Long> likedUsersIds;
    int genre_id;
    int mpa_id;
}

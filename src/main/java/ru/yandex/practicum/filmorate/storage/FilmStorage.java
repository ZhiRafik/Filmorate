package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;

public interface FilmStorage {

    Film addFilm(Film film);

    Collection<Film> getAll();

    Film updateFilm(Film film);

    boolean containsFilm(Film film);

    Film getFilm(Long filmId);
}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film);

    Collection<Film> getAll();

    Film updateFilm(Film film);

    boolean containsFilm(Film film);

    boolean addLike(Long userId, Long filmId);

    boolean removeLike(Long userId, Long filmId);

    boolean checkLike(Long userId, Long filmId);

    Optional<Film> getFilm(Long filmId);

    List<Film> getMostPopularFilms(int n);
}

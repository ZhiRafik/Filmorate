package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.enums.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;
    @Qualifier("filmDbStorage")
    private final FilmStorage filmStorage;
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    @Autowired
    public FilmController(FilmService filmService, FilmStorage filmStorage, UserStorage userStorage) {
        this.filmService = filmService;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        return filmStorage.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        log.info("Получен запрос на обновление фильма: {}", film);
        return filmStorage.updateFilm(film);
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получен запрос на получение фильмов");
        return filmStorage.getAll();
    }

    @PostMapping("/{filmId}/like/{userId}")
    public Film likeFilm(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен запрос на добавление лайка: filmId={}, userId={}", userId, filmId);
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId).orElse(null);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        return filmService.likeFilm(user, film);
    }

    @DeleteMapping("{filmId}/like/{userId}")
    public Film removeLikeFromFilm(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("Получен запрос на удаление лайка: filmId={}, userId={}", userId, filmId);
        User user = userStorage.getUser(userId);
        Film film = filmStorage.getFilm(filmId).orElse(null);
        if (film == null) {
            throw new NotFoundException("Фильм с id " + filmId + " не найден");
        }
        return filmService.removeLikeFromFilm(user, film);
    }

    @GetMapping("/popular?count={count}")
    public Collection<Film> getMostPopularFilms(@PathVariable(required = false) int count) {
        log.info("Получен запрос на получение популярных фильмов. Количество: {}", count);
        return filmService.getPopularFilms(count);
    }

    @GetMapping("/genres")
    public Collection<Genre> getGenres() {
        log.info("Получен запрос на получение списка жанров");
        return filmService.getGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenre(@PathVariable int id) {
        log.info("Получен запрос на получение жанра");
        return filmService.getGenre(id).orElse(null);
    }

}

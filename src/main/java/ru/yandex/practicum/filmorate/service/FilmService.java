package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.Genre;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Optional;

@Slf4j
@Service
public class FilmService {
    @Autowired
    @Qualifier("filmDbStorage")
    FilmStorage filmStorage;
    @Autowired
    @Qualifier("userDbStorage")
    UserStorage userStorage;

    public Film likeFilm(User user, Film film) {
        log.info("Получен запрос на добавление лайка: userId={}, filmId={}", user.getId(), film.getId());
        checkIfUserAndFilmExist(user, film);
        filmStorage.addLike(user.getId(), film.getId());
        if (filmStorage.checkLike(user.getId(), film.getId())) {
            log.info("Лайк добавлен: userId={}, filmId={}", user.getId(), film.getId());
        } else {
            log.warn("Лайк НЕ добавлен: userId={}, filmId={}", user.getId(), film.getId());
        }
        return film;
    }

    public Film removeLikeFromFilm(User user, Film film) {
        log.info("Получен запрос на удаление лайка: userId={}, filmId={}", user.getId(), film.getId());
        if (filmStorage.checkLike(user.getId(), film.getId())) {
            log.error("Ошибка: Пользователь с id={} не ставил лайк на фильм с id={}", user.getId(), film.getId());
            throw new NotFoundException("Пользователь не ставил лайк на этот фильм");
        }
        filmStorage.removeLike(user.getId(), film.getId());
        log.info("Лайк удалён. userId={} убрал лайк с фильма filmId={}", user.getId(), film.getId());
        return film;
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Получен запрос на получение {} популярных фильмов", count);
        ArrayList<Film> films = new ArrayList<>(filmStorage.getAll());
        films = (ArrayList<Film>) filmStorage.getMostPopularFilms(count);
        log.info("Фильмы отсортированы по количеству лайков. Общее количество фильмов: {}", films.size());
        return films;
    }

    private void checkIfUserAndFilmExist(User user, Film film) {
        log.debug("Проверка существования фильма с id={} и пользователя с id={}", film.getId(), user.getId());
        if (!filmStorage.containsFilm(film)) {
            log.error("Фильм с id={} не найден", film.getId());
            throw new NotFoundException("Указанный фильм не найден: " + film.toString());
        }
        if (!userStorage.containsUser(user)) {
            log.error("Пользователь с id={} не найден", user.getId());
            throw new NotFoundException("Указанный пользователь не найден: " + user.toString());
        }
        log.debug("Проверка завершена: фильм и пользователь существуют");
    }

    public Collection<Genre> getGenres() {
        log.info("Получен запрос на получение всех жанров");
        return filmStorage.getGenres();
    }

    public Optional<Genre> getGenre(int id) {
        log.info("Получен запрос на жанра по id");
        return filmStorage.getGenre(id);
    }
}

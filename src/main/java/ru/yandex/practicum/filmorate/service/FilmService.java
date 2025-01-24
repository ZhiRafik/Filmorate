package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Comparator;
import java.util.ArrayList;

@Slf4j
@Service
public class FilmService {
    @Autowired
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;

    public Film likeFilm(User user, Film film) {
        log.info("Получен запрос на добавление лайка: userId={}, filmId={}", user.getId(), film.getId());
        checkIfUserAndFilmExist(user, film);
        film.getLikedUsersIds().add(user.getId());
        log.info("Лайк добавлен: userId={}, filmId={}", user.getId(), film.getId());
        return film;
    }

    public Film removeLikeFromFilm(User user, Film film) {
        log.info("Получен запрос на удаление лайка: userId={}, filmId={}", user.getId(), film.getId());
        if (!film.getLikedUsersIds().contains(user.getId())) {
            log.error("Ошибка: Пользователь с id={} не ставил лайк на фильм с id={}", user.getId(), film.getId());
            throw new NotFoundException("Пользователь не ставил лайк на этот фильм");
        }
        film.getLikedUsersIds().remove(user.getId());
        log.info("Лайк удалён. userId={} убрал лайк с фильма filmId={}", user.getId(), film.getId());
        return film;
    }

    public Collection<Film> getPopularFilms(int count) {
        log.info("Получен запрос на получение {} популярных фильмов", count);
        ArrayList<Film> films = new ArrayList<>(filmStorage.getAll());
        films.sort(Comparator.comparingLong(film -> film.getLikedUsersIds().size()));
        log.info("Фильмы отсортированы по количеству лайков. Общее количество фильмов: {}", films.size());
        if (films.size() <= count) {
            log.info("Возвращаются все фильмы, так как их количество меньше или равно {}", count);
            return films;
        }
        log.info("Возвращаются {} самых популярных фильмов", count);
        return films.subList(films.size() - count, films.size());
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
}

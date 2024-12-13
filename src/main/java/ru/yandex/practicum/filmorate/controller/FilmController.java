package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.time.LocalDate;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final Map<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        film = getValidatedFilm(film);
        film.setId(getNextId());
        films.put(film.getId(), film);
        log.info("Фильм добавлен с ID={}. Текущее количество фильмов: {}", film.getId(), films.size());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на обновление фильма с ID: {}", film.getId());
        if (!films.containsKey(film.getId())) {
            log.error("Фильм с ID={} не найден", film.getId());
            throw new ValidationException("Фильм с указанным ID не найден");
        }
        film = getValidatedFilm(film);
        Film filmBefore = films.get(film.getId());
        filmBefore.setDescription(film.getDescription());
        filmBefore.setName(film.getName());
        filmBefore.setDuration(film.getDuration());
        filmBefore.setReleaseDate(film.getReleaseDate());
        log.info("Фильм с ID={} успешно обновлён", film.getId());
        return filmBefore;
    }

    @GetMapping
    public Collection<Film> getAll() {
        log.info("Получен запрос на получение всех фильмов. Всего фильмов: {}", films.size());
        return films.values();
    }

    private Film getValidatedFilm(Film film) {
        log.debug("Выполняется валидация фильма: {}", film);
        if (film.getName().isBlank() || film.getName() == null) {
            log.error("Ошибка валидации: название фильма пустое");
            throw new ValidationException("Name can not be blank");
        }
        if (film.getDescription().length() > 200) {
            log.error("Ошибка валидации: описание фильма превышает 200 символов");
            throw new ValidationException("Description can not consist more than 200 symbols");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.error("Ошибка валидации: дата релиза раньше 28 декабря 1895 года");
            throw new ValidationException("Date of release can not be before 28 December of 1895");
        }
        if (film.getDuration().isNegative()) {
            log.error("Ошибка валидации: продолжительность фильма отрицательная");
            throw new ValidationException("Duration of the film can not be negative");
        }
        log.debug("Фильм успешно прошёл валидацию: {}", film);
        return film;
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Сгенерирован новый ID для фильма: {}", currentMaxId++);
        return currentMaxId++;
    }

}

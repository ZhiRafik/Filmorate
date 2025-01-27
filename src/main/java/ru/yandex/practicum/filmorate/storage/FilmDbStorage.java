package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component("filmDbStorage")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    JdbcTemplate jdbc = new JdbcTemplate();
    RowMapper<Film> mapper = new FilmRowMapper();
    FilmRepository filmRepository = new FilmRepository(jdbc, mapper);

    @Override
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление фильма: {}", film);
        film = getValidatedFilm(film);
        film = filmRepository.addFilm(film);
        log.info("Фильм добавлен с ID={}.", film.getId());
        return film;
    }

    @Override
    public Collection<Film> getAll() {
        log.info("Получен запрос на получение всех фильмов.");
        return filmRepository.findAll();
    }

    @Override
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("Получен запрос на обновление фильма с ID: {}", film.getId());
        film = getValidatedFilm(film);
        film = filmRepository.update(film);
        log.info("Фильм с ID={} успешно обновлён", film.getId());
        return film;
    }

    @Override
    public boolean containsFilm(Film film) {
        if (filmRepository.findById(film.getId()).isPresent()) {
            return true;
        }
        return false;
    }

    @Override
    public Optional<Film> getFilm(Long filmId) {
        if (filmRepository.findById(filmId).isPresent()) {
            return filmRepository.findById(filmId);
        }
        throw new NotFoundException("Фильм с таким id не найден");
    }

    @Override
    public boolean addLike(Long userId, Long filmId) {
        return filmRepository.addLike(userId, filmId);
    }

    @Override
    public boolean checkLike(Long userId, Long filmId) {
        return filmRepository.checkLike(userId, filmId);
    }

    @Override
    public boolean removeLike(Long userId, Long filmId) {
        return filmRepository.removeLike(userId, filmId);
    }

    @Override
    public List<Film> getMostPopularFilms(int n) {
        return filmRepository.getMostPopularFilms(n);
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
}

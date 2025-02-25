import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmTest {

    private final FilmService filmService = new FilmService();
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final FilmController filmController = new FilmController(filmService, filmStorage, userStorage);

    @Test
    void shouldThrowExceptionWhenNameIsBlank() {
        Film film = new Film();
        film.setName("  ");
        film.setDescription("Valid description");
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertEquals("Name can not be blank", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDescriptionTooLong() {
        Film film = new Film();
        film.setName("Valid name");
        film.setDescription("A".repeat(201));
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertEquals("Description can not consist more than 200 symbols", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenReleaseDateIsTooEarly() {
        Film film = new Film();
        film.setName("Valid name");
        film.setDescription("Valid description");
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.of(1800, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertEquals("Date of release can not be before 28 December of 1895", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNegative() {
        Film film = new Film();
        film.setName("Valid name");
        film.setDescription("Valid description");
        film.setDuration(Duration.ofMinutes(-10));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            filmController.addFilm(film);
        });

        assertEquals("Duration of the film can not be negative", exception.getMessage());
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        Film film = new Film();
        film.setName("Valid name");
        film.setDescription("Valid description");
        film.setDuration(Duration.ofMinutes(90));
        film.setReleaseDate(LocalDate.of(2000, 1, 1));

        assertDoesNotThrow(() -> filmController.addFilm(film));
    }
}
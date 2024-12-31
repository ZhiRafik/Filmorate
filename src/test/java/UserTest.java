import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final UserService userService = new UserService();
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage = new InMemoryUserStorage();
    UserController userController = new UserController(userService, filmStorage, userStorage);

    @Test
    void shouldThrowExceptionWhenEmailDoesNotContainAt() {
        User user = new User();
        user.setEmail("invalidEmail");
        user.setLogin("validLogin");
        user.setName("John");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });

        assertEquals("Email can not be blank and must contain @", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmailContainsAtButIsInvalid() {
        User user = new User();
        user.setEmail("invalid-Email@");
        user.setLogin("validLogin");
        user.setName("Valid Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });

        assertEquals("Email can not be blank and must contain @", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenLoginContainsSpaces() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("invalid login");
        user.setName("John");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });

        assertEquals("Login can not be blank and contain whitespaces", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenBirthdayIsInTheFuture() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("validLogin");
        user.setName("John");
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class, () -> {
            userController.addUser(user);
        });

        assertEquals("Birthday can not be in the future", exception.getMessage());
    }

    @Test
    void shouldPassValidationWhenAllFieldsAreValid() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setLogin("validLogin");
        user.setName("John");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        assertDoesNotThrow(() -> userController.addUser(user));
    }
}
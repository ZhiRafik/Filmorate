package ru.yandex.practicum;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.enums.Genre;
import ru.yandex.practicum.filmorate.enums.MPA;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private final UserService userService = new UserService();
    private final FilmStorage filmStorage = new InMemoryFilmStorage() {
        @Override
        public List<Film> getMostPopularFilms(int n) {
            return null;
        }

        @Override
        public Collection<Genre> getGenres() {
            return null;
        }

        @Override
        public Optional<Genre> getGenre(int id) {
            return null;
        }

        @Override
        public Collection<MPA> getMPAs() {
            return null;
        }

        @Override
        public Optional<MPA> getMPA(int id) {
            return null;
        }
    };
    private final UserStorage userStorage = new InMemoryUserStorage() {
        @Override
        public boolean addFriend(User user1, User user2) {
            return false;
        }

        @Override
        public boolean removeFriend(User user1, User user2) {
            return false;
        }

        @Override
        public Collection<Integer> getFriendsIds(User user) {
            return null;
        }

        @Override
        public Collection<Integer> getCommonFriendsIds(User user1, User user2) {
            return null;
        }
    };
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
package ru.yandex.practicum.filmorate.storage;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.dal.FilmRepository;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.regex.Pattern;

@Slf4j
@Component("userDbStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbc;
    @Qualifier("userRowMapper")
    private final RowMapper<User> mapper;
    private final UserRepository userRepository;

    @Override
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        user = getValidatedUser(user);
        user = userRepository.addUser(user);
        log.info("Пользователь добавлен с ID: {}.", user.getId());
        return user;
    }

    @Override
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя с ID: {}", user.getId());
        if (userRepository.containsUser(user.getId())) {
            log.error("Пользователь с ID {} не найден для обновления", user.getId());
            throw new ValidationException("Пользователь с указанным ID не найден");
        }
        user = getValidatedUser(user);
        user = userRepository.update(user);
        log.info("Пользователь с ID {} успешно обновлён", user.getId());
        return user;
    }

    @Override
    public User getUser(long userId) {
        if (!userRepository.containsUser(userId)) {
            throw new NotFoundException("Пользователь с таким ID не найден");
        }
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    public Collection<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public boolean containsUser(User user) {
        return userRepository.containsUser(user.getId());
    }

    @Override
    public boolean addFriend(User user1, User user2) {
        return userRepository.addFriend(user1, user2);
    }

    @Override
    public boolean removeFriend(User user1, User user2) {
        return userRepository.removeFriend(user1, user2);
    }

    @Override
    public Collection<Integer> getFriendsIds(User user) {
        return userRepository.getFriendsIds(user);
    }

    @Override
    public Collection<Integer> getCommonFriendsIds(User user1, User user2) {
        return userRepository.getCommonFriendsIds(user1, user2);
    }

    private static User getValidatedUser(User user) {
        log.debug("Выполняется валидация пользователя: {}", user);
        if (!isValidEmail(user.getEmail())) {
            log.error("Ошибка валидации: некорректный email");
            throw new ValidationException("Email can not be blank and must contain @");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.error("Ошибка валидации: некорректный логин");
            throw new ValidationException("Login can not be blank and contain whitespaces");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Ошибка валидации: день рождения в будущем");
            throw new ValidationException("Birthday can not be in the future");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя пустое, используется логин вместо имени");
            user.setName(user.getLogin());
        }
        log.debug("Пользователь успешно прошёл валидацию: {}", user);
        return user;
    }

    private static boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email != null && !email.isBlank() && Pattern.matches(emailRegex, email);
    }
}

package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.regex.Pattern;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User addUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        user = getValidatedUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь добавлен с ID: {}. Текущее количество пользователей: {}", user.getId(), users.size());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("Получен запрос на обновление пользователя с ID: {}", user.getId());
        if (!users.containsKey(user.getId())) {
            log.error("Пользователь с ID {} не найден для обновления", user.getId());
            throw new ValidationException("Пользователь с указанным ID не найден");
        }
        user = getValidatedUser(user);
        User userBefore = users.get(user.getId());
        userBefore.setName(user.getName());
        userBefore.setBirthday(user.getBirthday());
        userBefore.setLogin(user.getLogin());
        userBefore.setEmail(user.getEmail());
        log.info("Пользователь с ID {} успешно обновлён", user.getId());
        return userBefore;
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

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        log.debug("Сгенерирован новый ID для пользователя: {}", currentMaxId++);
        return currentMaxId++;
    }
}

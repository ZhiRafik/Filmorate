package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public UserController(UserService userService, FilmStorage filmStorage, UserStorage userStorage) {
        this.userService = userService;
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    @PostMapping
    public User addUser(@RequestBody User user) {
        log.info("Получен запрос на добавление пользователя: {}", user);
        return userStorage.addUser(user);
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        log.info("Получен запрос на обновление пользователя: {}", user);
        return userStorage.updateUser(user);
    }

    @PostMapping("/{userId}/friends/{friendId}")
    public Map<User, User> addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на добавление друга: userId={}, friendId={}", userId, friendId);
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        return userService.addFriend(user, friend);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public Map<User, User> removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на удаление друга: userId={}, friendId={}", userId, friendId);
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        return userService.removeFriend(user, friend);
    }

    @GetMapping
    public Collection<User> getAll() {
        log.info("Получен запрос на получения списка всех пользователей");
        return userService.getAll();
    }

    @GetMapping("/{userId}/friends")
    public Collection<User> getFriends(@PathVariable Long userId) {
        log.info("Получен запрос на получение списка друзей пользователя: userId={}", userId);
        User user = userStorage.getUser(userId);
        return userService.getFriends(user);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public Collection<User> getMutualFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("Получен запрос на получение общих друзей: userId={}, friendId={}", userId, friendId);
        User user = userStorage.getUser(userId);
        User friend = userStorage.getUser(friendId);
        return userService.getCommonFriends(user, friend);
    }

}

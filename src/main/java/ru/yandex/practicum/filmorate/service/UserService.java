package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Map;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    @Autowired
    @Qualifier("userDbStorage")
    UserStorage userStorage;

    public boolean addFriend(User user1, User user2) {
        log.info("Получен запрос на добавление друга: user1={}, user2={}", user1, user2);
        checkIfUsersExist(user1, user2);
        return userStorage.addFriend(user1, user2);
    }

    public boolean removeFriend(User user1, User user2) {
        log.info("Получен запрос на удаление друга: user1={}, user2={}", user1, user2);
        checkIfUsersExist(user1, user2);
        return userStorage.removeFriend(user1, user2);
    }

    public ArrayList<User> getFriends(User user) {
        log.info("Получен запрос на получение списка друзей пользователя: {}", user);
        checkIfUserExist(user);
        ArrayList<User> friends = new ArrayList<>();
        ArrayList<Integer> friendsIds = new ArrayList<>();
        friendsIds = (ArrayList<Integer>) userStorage.getFriendsIds(user);
        for (Integer id : friendsIds) {
            friends.add(userStorage.getUser(id));
        }
        log.info("Список друзей пользователя с id={} получен. Количество друзей: {}", user.getId(), friends.size());
        return friends;
    }

    public Collection<User> getAll() {
        log.info("Получен запрос на получение списка всех пользователей");
        return userStorage.getAll();
    }

    public Collection<User> getCommonFriends(User user1, User user2) {
        log.info("Получен запрос на получение общих друзей: user1={}, user2={}", user1, user2);
        checkIfUsersExist(user1, user2);
        Collection<Integer> commonFriendsIds = userStorage.getCommonFriendsIds(user1, user2);
        Collection<User> commonFriends = commonFriendsIds.stream()
                        .map(userStorage::getUser)
                        .collect(Collectors.toList());
        log.info("Общие друзья пользователей с id={} и id={} найдены. Количество общих друзей: {}", user1.getId(), user2.getId(), commonFriends.size());
        return commonFriends;
    }

    private void checkIfUsersExist(User user1, User user2) {
        log.debug("Проверка существования пользователей: user1={}, user2={}", user1, user2);
        checkIfUserExist(user1);
        checkIfUserExist(user2);
    }

    private void checkIfUserExist(User user) {
        log.debug("Проверка существования пользователя: {}", user);
        if (!userStorage.containsUser(user)) {
            log.error("Пользователь не найден: {}", user);
            throw new NotFoundException("Пользователь с id не найден: " + user.toString());
        }
    }
}

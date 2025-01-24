package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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

@Slf4j
@Service
public class UserService {
    @Autowired
    UserStorage userStorage;

    public Map<User, User> addFriend(User user1, User user2) {
        log.info("Получен запрос на добавление друга: user1={}, user2={}", user1, user2);
        checkIfUsersExist(user1, user2);
        user1.getFriendsIds().add(user2.getId());
        user2.getFriendsIds().add(user1.getId());
        log.info("Друзья добавлены: user1={}, user2={}", user1, user2);
        return Map.of(user1, user2);
    }

    public Map<User, User> removeFriend(User user1, User user2) {
        log.info("Получен запрос на удаление друга: user1={}, user2={}", user1, user2);
        checkIfUsersExist(user1, user2);
        if (!user1.getFriendsIds().contains(user2.getId())) {
            log.error("Пользователь с id={} не найден в списке друзей пользователя с id={}", user2.getId(), user1.getId());
            throw new ValidationException("Пользователь не найден в списке друзей");
        }
        user1.getFriendsIds().remove(user2.getId());
        user2.getFriendsIds().remove(user1.getId());
        log.info("Друзья удалены: user1={}, user2={}", user1, user2);
        return Map.of(user1, user2);
    }

    public Collection<User> getFriends(User user) {
        log.info("Получен запрос на получение списка друзей пользователя: {}", user);
        checkIfUserExist(user);
        ArrayList<User> friends = new ArrayList<>();
        Set<Long> friendsIds = user.getFriendsIds();
        for (Long id : friendsIds) {
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
        Set<Long> commonFriendsIds = new HashSet<>(user1.getFriendsIds());
        commonFriendsIds.retainAll(user2.getFriendsIds());
        ArrayList<User> commonFriends = new ArrayList<>(commonFriendsIds.size());
        for (Long id : commonFriendsIds) {
            commonFriends.add(userStorage.getUser(id));
        }
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

package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.EventDao;
import ru.yandex.practicum.filmorate.dal.FilmDao;
import ru.yandex.practicum.filmorate.dal.UserDao;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserDao userDao;
    private final FilmDao filmDao;
    private final EventDao eventDao;

    public User createUser(User user) {
        updateName(user);
        Long id = userDao.insert(user);
        user.setId(id);
        return user;
    }

    public User updateUser(User newUser) {
        userDao.checkUserExistsOrThrow(newUser.getId());
        User oldUser = userDao.getUser(newUser.getId());
        User result = update(oldUser, newUser);
        userDao.update(result);
        return result;
    }

    public Collection<User> getUsers() {
        return userDao.getUsers();
    }

    public User getUser(long id) {
        userDao.checkUserExistsOrThrow(id);
        return userDao.getUser(id);
    }

    public void addFriend(long userId, long friendId) {
        userDao.checkUserExistsOrThrow(userId);
        userDao.checkUserExistsOrThrow(friendId);
        userDao.addFriend(userId, friendId);
        eventDao.insert(userId, "FRIEND", "ADD", friendId);
    }

    public void removeFriend(long userId, long friendId) {
        userDao.checkUserExistsOrThrow(userId);
        userDao.checkUserExistsOrThrow(friendId);
        userDao.removeFriend(userId, friendId);
        eventDao.insert(userId, "FRIEND", "REMOVE", friendId);
    }

    public Set<User> getFriends(long userId) {
        userDao.checkUserExistsOrThrow(userId);
        return userDao.getFriends(userId);
    }

    public Set<User> getCommonFriends(long userId, long otherUserId) {
        userDao.checkUserExistsOrThrow(userId);
        userDao.checkUserExistsOrThrow(otherUserId);
        Set<User> result = new HashSet<>(userDao.getFriends(userId));
        result.retainAll(userDao.getFriends(otherUserId));
        return result;
    }

    public Map<String, String> deleteUser(long userId) {
        userDao.checkUserExistsOrThrow(userId);
        userDao.delete(userId);
        return Map.of("description", String.format("Пользователь с идентификатором: %d успешно удален.", userId));
    }

    public Collection<Film> getRecommendations(long userId) {
        userDao.checkUserExistsOrThrow(userId);
        if (filmDao.getLikeFilms(userId).isEmpty()) {
            return Collections.emptyList();
        }
        Long idWithSharedLikes = userDao.getUserIdWithSharedLikes(userId);
        if (idWithSharedLikes == null) { // id пользователя с максимальным количеством схожих лайков
            return Collections.emptyList(); // если нет пользователя со схожими лайками, то нет и рекомендаций
        }
        return filmDao.findRecommendedFilms(idWithSharedLikes, userId);
    }

    public Collection<Long> getLikeFilms(long userId) {
        userDao.checkUserExistsOrThrow(userId);
        return filmDao.getLikeFilms(userId);
    }

    public List<Event> getEvents(long id) {
        userDao.checkUserExistsOrThrow(id);
        return eventDao.getEvents(id);
    }

    private void updateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

    private User update(User originalUser, User newUser) {
        return User.builder()
                .id(newUser.getId())
                .name(newUser.getName() == null ? originalUser.getName() : newUser.getName())
                .login(newUser.getLogin() == null ? originalUser.getLogin() : newUser.getLogin())
                .email(newUser.getEmail() == null ? originalUser.getEmail() : newUser.getEmail())
                .birthday(newUser.getBirthday() == null ? originalUser.getBirthday() : newUser.getBirthday())
                .build();
    }
}

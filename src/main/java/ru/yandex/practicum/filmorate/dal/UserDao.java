package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserDao {

    Long insert(User user);

    Long update(User user);

    boolean delete(long userId);

    List<User> getUsers();

    User getUser(long userId);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);

    Set<User> getFriends(long userId);

    Long getUserIdWithSharedLikes(long userId);

    void checkUserExistsOrThrow(long userId);
}

package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    User getUser(long userId);

    Collection<User> getAll();

    boolean containsUser(User user);

    boolean addFriend(User user1, User user2);

    boolean removeFriend(User user1, User user2);

    Collection<Integer> getFriendsIds(User user);

    Collection<Integer> getCommonFriendsIds(User user1, User user2);
}

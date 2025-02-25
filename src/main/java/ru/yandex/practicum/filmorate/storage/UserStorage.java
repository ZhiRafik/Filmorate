package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user);

    User getUser(long userId);

    Collection<User> getAll();

    boolean containsUser(User user);
}

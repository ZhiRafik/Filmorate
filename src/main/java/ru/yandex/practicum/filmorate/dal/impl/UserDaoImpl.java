package ru.yandex.practicum.filmorate.dal.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.UserDao;
import ru.yandex.practicum.filmorate.dal.base.BaseDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

import static ru.yandex.practicum.filmorate.dal.impl.query.UserQuery.*;

@Primary
@Component
public class UserDaoImpl extends BaseDao<User> implements UserDao {

    public UserDaoImpl(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Long insert(User user) {
        return insert(CREATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday());
    }

    @Override
    public Long update(User user) {
        update(UPDATE_USER_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());
        return user.getId();
    }

    @Override
    public boolean delete(long userId) {
        return remove(DELETE_USER_QUERY, userId);
    }

    @Override
    public List<User> getUsers() {
        return findMany(GET_USERS_QUERY);
    }

    @Override
    public User getUser(long userId) {
        return findOne(GET_USER_QUERY, userId);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        insert(ADD_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        remove(REMOVE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public Set<User> getFriends(long userId) {
        return Set.copyOf(findMany(GET_FRIENDS_QUERY, userId));
    }

    @Override
    public Long getUserIdWithSharedLikes(long userId) {
        try {
            return jdbc.queryForObject(GET_USER_WITH_SHARED_LIKES_QUERY, Long.class, userId, userId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public void checkUserExistsOrThrow(long userId) {
        int count = jdbc.queryForObject(GET_USERS_COUNT_QUERY, Integer.class, userId);
        if (count == 0) {
            throw new NotFoundException("Пользователя с таким идентификатором не существует");
        }
    }
}
package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.impl.UserDaoImpl;
import ru.yandex.practicum.filmorate.mapper.row.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDaoImpl.class, UserRowMapper.class})
class UserDaoImplTest {

    private final UserDaoImpl userDao;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setUp() {
        user1 = User.builder()
                .email("user1@example.com")
                .login("user1")
                .name("User One")
                .birthday(LocalDate.of(1991, 2, 2))
                .build();

        user2 = User.builder()
                .email("user2@example.com")
                .login("user2")
                .name("User Two")
                .birthday(LocalDate.of(1992, 3, 3))
                .build();

        user3 = User.builder()
                .email("user3@example.com")
                .login("user3")
                .name("User Three")
                .birthday(LocalDate.of(1993, 4, 4))
                .build();
    }

    @Test
    public void testUpsertUser() {
        Long userId = userDao.insert(user1);

        assertNotNull(userId);
        User savedUser = userDao.getUser(userId);

        assertNotNull(savedUser);
        assertEquals(savedUser.getEmail(), user1.getEmail());
    }

    @Test
    public void testGetUsers() {
        userDao.insert(user1);
        userDao.insert(user2);

        List<User> users = userDao.getUsers();
        assertEquals(2, users.size());
    }

    @Test
    public void testGetUser() {
        Long userId = userDao.insert(user2);
        User foundUser = userDao.getUser(userId);

        assertNotNull(foundUser);
        assertEquals(foundUser.getEmail(), user2.getEmail());
    }

    @Test
    public void testAddAndRemoveFriend() {
        Long userId1 = userDao.insert(user1);
        Long userId2 = userDao.insert(user2);

        userDao.addFriend(userId1, userId2);
        Set<User> friends = userDao.getFriends(userId1);

        assertTrue(friends.stream().anyMatch(user -> user.getId().equals(userId2)));

        userDao.removeFriend(userId1, userId2);
        friends = userDao.getFriends(userId1);
        assertFalse(friends.stream().anyMatch(user -> user.getId().equals(userId2)));
    }

    @Test
    public void testGetFriends() {
        Long userId1 = userDao.insert(user1);
        Long userId2 = userDao.insert(user2);
        Long userId3 = userDao.insert(user3);

        userDao.addFriend(userId1, userId2);
        userDao.addFriend(userId1, userId3);

        Set<User> friends = userDao.getFriends(userId1);

        assertEquals(2, friends.size());
        assertTrue(friends.stream().anyMatch(user -> user.getId().equals(userId2)));
        assertTrue(friends.stream().anyMatch(user -> user.getId().equals(userId3)));
    }
}
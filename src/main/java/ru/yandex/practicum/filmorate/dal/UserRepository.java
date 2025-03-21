package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Repository
public class UserRepository extends BaseRepository<User> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM users";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM users WHERE email = ?";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM users WHERE id = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(username, email, password, registration_date)" +
                                                "VALUES (?, ?, ?, ?) returning id";
    private static final String UPDATE_QUERY = "UPDATE users SET username = ?, email = ?, password = ? WHERE id = ?";
    private static final String ADD_FRIEND = "INSERT INTO friends_relationship(first_user, second_user, " +
                                             "friendship_status_id) VALUES (?, ?, ?) " +
                                             "ON CONFLICT (first_user, second_user) " +
                                             "DO UPDATE SET friendship_status_id = 1 " +
                                             "WHERE friends_relationship.first_user = EXCLUDED.first_user" +
                                             "AND friends_relationship.second_user = EXCLUDED.second_user";
    private static final String GET_FRIENDSHIP_STATUS_ID = "SELECT friendship_status_id " +
                                                            "FROM friendship_status " +
                                                            "WHERE name = ?";
    private static final String REMOVE_FRIEND = "DELETE FROM friends_relationship " +
                                                "WHERE first_user = ? AND second_user = ?";
    private static final String GET_FRIENDS_IDS = "SELECT second_user FROM friends_relationship " +
                                                  "WHERE first_user = ?";

    public UserRepository(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    public List<User> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<User> findByEmail(String email) {
        return findOne(FIND_BY_EMAIL_QUERY, email);
    }

    public Optional<User> findById(long userId) {
        return findOne(FIND_BY_ID_QUERY, userId);
    }

    public Collection<Integer> getFriendsIds(User user) {
        return jdbc.query(GET_FRIENDS_IDS, new Object[]{user.getId()},
                (rs, rowNum) -> rs.getInt("second_user"));
    }

    public Collection<Integer> getCommonFriendsIds(User user1, User user2) {
        Collection<Integer> friendsIds1 = getFriendsIds(user1);
        Collection<Integer> friendsIds2 = getFriendsIds(user2);
        Collection<Integer> commonFriends = new ArrayList<>(friendsIds1);
        commonFriends.retainAll(friendsIds2);
        return commonFriends;
    }

    public boolean removeFriend(User user1, User user2) {
        int rowsAffected = jdbc.update(REMOVE_FRIEND, user1.getId(), user2.getId());
        return rowsAffected > 0;
    }

    public boolean addFriend(User user1, User user2) {
        Integer friendshipStatusId = jdbc.queryForObject(GET_FRIENDSHIP_STATUS_ID, Integer.class, "CONFIRMED");
        int rowsAffected = jdbc.update(ADD_FRIEND, user1.getId(), user2.getId(), friendshipStatusId);
        return rowsAffected > 0;
    }

    public boolean containsUser(long userId) {
        if (findById(userId).isPresent()) {
            return true;
        }
        return false;
    }

    public User addUser(User user) {
        long id = insert(
                INSERT_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    public User update(User user) {
        update(
                UPDATE_QUERY,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday()
        );
        return user;
    }
}
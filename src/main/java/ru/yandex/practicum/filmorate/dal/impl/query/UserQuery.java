package ru.yandex.practicum.filmorate.dal.impl.query;

public class UserQuery {

    public static final String CREATE_USER_QUERY = "INSERT INTO users (email, login, name, birthday) " +
            "VALUES (?, ?, ?, ?)";

    public static final String UPDATE_USER_QUERY = "UPDATE users " +
            "SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";

    public static final String DELETE_USER_QUERY = """
            DELETE FROM users
            WHERE id = ?;
            """;

    public static final String GET_USERS_QUERY = "SELECT * FROM users";

    public static final String GET_USER_QUERY = "SELECT * FROM users WHERE id = ?";

    public static final String ADD_FRIEND_QUERY = "INSERT INTO friend_links (user_id, friend_id) VALUES (?, ?)";

    public static final String REMOVE_FRIEND_QUERY = "DELETE FROM friend_links " +
            "WHERE user_id = ? AND friend_id = ?";

    public static final String GET_FRIENDS_QUERY = "SELECT u.* FROM users u " +
            "JOIN friend_links f ON u.id = f.friend_id WHERE f.user_id = ?";


    public static final String GET_USERS_COUNT_QUERY = "SELECT COUNT(*) FROM users WHERE id = ?";

    public static final String GET_USER_WITH_SHARED_LIKES_QUERY = """
            SELECT otherUserLikes.user_id
            FROM film_likes likes
            JOIN film_likes otherUserLikes
                ON likes.film_id = otherUserLikes.film_id
            WHERE likes.user_id = ?
                AND otherUserLikes.user_id != ?
            GROUP BY otherUserLikes.user_id
            ORDER BY COUNT(*) DESC
            LIMIT 1;
            """;
}

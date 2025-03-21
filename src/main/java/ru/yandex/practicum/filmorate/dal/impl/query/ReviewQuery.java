package ru.yandex.practicum.filmorate.dal.impl.query;

public class ReviewQuery {

    public static final String INSERT_REVIEW_QUERY = "INSERT INTO reviews (content, is_positive, user_id, film_id) VALUES (?, ?, ?, ?)";

    public static final String UPDATE_REVIEW_QUERY = "UPDATE reviews SET content = ?, is_positive = ?, user_id = ?, film_id = ? WHERE id = ?";

    public static final String DELETE_REVIEW_QUERY = "DELETE FROM reviews WHERE id = ?";

    public static final String COUNT_REVIEW_QUERY = "SELECT COUNT(*) FROM reviews where id = ?";

    public static final String INSERT_MARK_REVIEW_QUERY = "INSERT INTO reviews_marks (review_id, user_id, mark_type) VALUES (?, ?, ?)";

    public static final String DELETE_MARK_REVIEW_QUERY = "DELETE FROM reviews_marks WHERE review_id = ? AND user_id = ?";

    public static final String FIND_REVIEW_BY_ID_AND_COUNT_QUERY = """
            SELECT DISTINCT r.id, r.content, r.is_positive, r.user_id, r.film_id, rm.useful
                FROM reviews r
                LEFT JOIN (SELECT review_id, SUM(mark_type) useful FROM reviews_marks GROUP BY id) rm ON r.id = rm.review_id
                WHERE film_id = ?
                ORDER BY rm.useful DESC
                LIMIT ?""";

    public static final String FIND_REVIEW_BY_COUNT_QUERY = """
            SELECT DISTINCT r.id, r.content, r.is_positive, r.user_id, r.film_id, rm.useful
                FROM reviews r
                LEFT JOIN (SELECT review_id, SUM(mark_type) useful FROM reviews_marks GROUP BY id) rm ON r.id = rm.review_id
                LIMIT ?""";

    public static final String QUERY_FIND_REVIEW_BY_ID = """
            SELECT DISTINCT r.id, r.content, r.is_positive, r.user_id, r.film_id, rm.useful
                FROM reviews r
                LEFT JOIN (SELECT review_id, SUM(mark_type) useful FROM reviews_marks GROUP BY id) rm ON r.id = rm.review_id
                WHERE r.id = ?
                LIMIT 1""";
}

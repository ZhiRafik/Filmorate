package ru.yandex.practicum.filmorate.dal.impl.query;

public class FilmQuery {

    public static final String COUNT_FILM_QUERY = "SELECT COUNT(*) FROM films WHERE id = ?";

    public static final String CREATE_FILM_QUERY = "INSERT INTO films " +
            "(name, description, release_date, duration, mpa_id, director_id) VALUES (?, ?, ?, ?, ?, ?)";

    public static final String UPDATE_FILM_QUERY = "UPDATE films " +
            "SET name = ?, description = ?, release_date = ?, duration = ?, mpa_id = ?, director_id = ? WHERE id = ?";

    public static final String GET_FILMS_QUERY = """
            SELECT f.id AS film_id, f.name, f.description, f.release_date,
                   f.duration, mpa.id AS mpa_id, mpa.name AS mpa_name,
                   dir.id AS dir_id, dir.name AS dir_name,
                   (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                   (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                     FROM films_genres fg
                     JOIN genres gt ON gt.id = fg.genre_id
                     WHERE fg.film_id = f.id) AS genres
            FROM films f
            JOIN mpa ON f.mpa_id = mpa.id
            LEFT JOIN directors dir ON f.director_id = dir.id
            """;

    public static final String GET_FILM_QUERY = """
            SELECT f.id AS film_id, f.name, f.description, f.release_date, f.duration,
                   mpa.id AS mpa_id, mpa.name AS mpa_name, dir.id AS dir_id, dir.name AS dir_name,
                   (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                   (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                     FROM films_genres fg
                     JOIN genres gt ON gt.id = fg.genre_id
                     WHERE fg.film_id = f.id) AS genres
            FROM films f
            JOIN mpa ON f.mpa_id = mpa.id
            LEFT JOIN directors dir ON f.director_id = dir.id
            WHERE f.id = ?
            """;

    public static final String DELETE_FILM_BY_ID_QUERY = """
            DELETE FROM films
            WHERE id = ?;
            """;

    public static final String ADD_LIKE_QUERY = "INSERT INTO film_likes (film_id, user_id) VALUES (?, ?)";

    public static final String REMOVE_LIKE_QUERY = "DELETE FROM film_likes WHERE film_id = ? AND user_id = ?";

    public static final String INSERT_FILM_GENRE_QUERY = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";

    public static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM films_genres WHERE film_id = ?";

    public static final String DIRECTOR_FIND_ON_LIKES_QUERY = """
                SELECT f.id AS film_id,
                       f.name,
                       f.description,
                       f.release_date,
                       f.duration,
                       mpa.id AS mpa_id,
                       mpa.name AS mpa_name,
                       dir.id AS dir_id,
                       dir.name AS dir_name,
                       (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                       (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                         FROM films_genres fg
                         JOIN genres gt ON gt.id = fg.genre_id
                         WHERE fg.film_id = f.id) AS genres
                FROM films f
                JOIN mpa ON f.mpa_id = mpa.id
                LEFT JOIN directors dir ON f.director_id = dir.id
                WHERE dir.id = ?
                ORDER BY likes DESC;
            """;

    public static final String DIRECTOR_ON_YEAR_QUERY = """
                SELECT f.id AS film_id,
                       f.name,
                       f.description,
                       f.release_date,
                       f.duration,
                       mpa.id AS mpa_id,
                       mpa.name AS mpa_name,
                       dir.id AS dir_id,
                       dir.name AS dir_name,
                       (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                       (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                         FROM films_genres fg
                         JOIN genres gt ON gt.id = fg.genre_id
                         WHERE fg.film_id = f.id) AS genres
                FROM films f
                JOIN mpa ON f.mpa_id = mpa.id
                LEFT JOIN directors dir ON f.director_id = dir.id
                WHERE dir.id = ?
                ORDER BY EXTRACT(YEAR FROM f.release_date) ASC;
            """;

    public static final String FIND_RECOMMENDED_FILMS_QUERY = """
                SELECT
                    f.id AS film_id,
                    f.name,
                    f.description,
                    f.release_date,
                    f.duration,
                    mpa.id AS mpa_id,
                    mpa.name AS mpa_name,
                    dir.id AS dir_id,
                    dir.name AS dir_name,
                    (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                    (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                     FROM films_genres fg
                     JOIN genres gt ON gt.id = fg.genre_id
                     WHERE fg.film_id = f.id) AS genres
                FROM films f
                JOIN mpa mpa ON f.mpa_id = mpa.id
                LEFT JOIN directors dir ON f.director_id = dir.id
                WHERE f.id IN (
                    SELECT DISTINCT otherUserLikes.film_id
                    FROM film_likes otherUserLikes
                    WHERE otherUserLikes.user_id = ?
                    AND otherUserLikes.film_id NOT IN (
                        SELECT likes.film_id
                        FROM film_likes likes
                        WHERE likes.user_id = ?
                    )
                );
            """;

    public static final String FIND_POPULAR_FILMS_GENRE_YEAR_QUERY = """
                SELECT f.id AS film_id,
                       f.name,
                       f.description,
                       f.release_date,
                       f.duration,
                       mpa.id AS mpa_id,
                       mpa.name AS mpa_name,
                       dir.id AS dir_id,
                       dir.name AS dir_name,
                       (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                       (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                         FROM films_genres fg
                         JOIN genres gt ON gt.id = fg.genre_id
                         WHERE fg.film_id = f.id) AS genres
                FROM films f
                JOIN mpa mpa ON f.mpa_id = mpa.id
                LEFT JOIN directors dir ON f.director_id = dir.id
                LEFT JOIN films_genres fg ON f.id = fg.film_id
                WHERE (fg.genre_id = ?)
                  AND (EXTRACT(YEAR FROM f.release_date) = ?)
                GROUP BY f.id, mpa.id, mpa.name, dir.id, dir.name
                ORDER BY likes DESC
                LIMIT ?;
            """;

    public static final String FIND_POPULAR_FILMS_YEAR_QUERY = """
            SELECT f.id AS film_id,
                       f.name,
                       f.description,
                       f.release_date,
                       f.duration,
                       mpa.id AS mpa_id,
                       mpa.name AS mpa_name,
                       dir.id AS dir_id,
                       dir.name AS dir_name,
                       (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                       (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                         FROM films_genres fg
                         JOIN genres gt ON gt.id = fg.genre_id
                         WHERE fg.film_id = f.id) AS genres
                FROM films f
                JOIN mpa ON f.mpa_id = mpa.id
                LEFT JOIN directors dir ON f.director_id = dir.id
                WHERE EXTRACT(YEAR FROM f.release_date) = ?
                GROUP BY f.id, mpa.id, mpa.name, dir.id, dir.name
                ORDER BY likes DESC
                LIMIT ?;
            """;

    public static final String FIND_POPULAR_FILMS_GENRE_QUERY = """
            SELECT f.id AS film_id,
                       f.name,
                       f.description,
                       f.release_date,
                       f.duration,
                       mpa.id AS mpa_id,
                       mpa.name AS mpa_name,
                       dir.id AS dir_id,
                       dir.name AS dir_name,
                       (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                       (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                         FROM films_genres fg
                         JOIN genres gt ON gt.id = fg.genre_id
                         WHERE fg.film_id = f.id) AS genres
                FROM films f
                JOIN mpa ON f.mpa_id = mpa.id
                LEFT JOIN directors dir ON f.director_id = dir.id
                LEFT JOIN films_genres fg ON f.id = fg.film_id
                WHERE fg.genre_id = ?
                GROUP BY f.id, mpa.id, mpa.name, dir.id, dir.name
                ORDER BY likes DESC
                LIMIT ?;
            """;

    public static final String FIND_POPULAR_FILMS_QUERY = """
            SELECT f.id AS film_id,
                       f.name,
                       f.description,
                       f.release_date,
                       f.duration,
                       mpa.id AS mpa_id,
                       mpa.name AS mpa_name,
                       dir.id AS dir_id,
                       dir.name AS dir_name,
                       (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                       (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                         FROM films_genres fg
                         JOIN genres gt ON gt.id = fg.genre_id
                         WHERE fg.film_id = f.id) AS genres
                FROM films f
                JOIN mpa ON f.mpa_id = mpa.id
                LEFT JOIN directors dir ON f.director_id = dir.id
                GROUP BY f.id, mpa.id, mpa.name, dir.id, dir.name
                ORDER BY likes DESC
                LIMIT ?;
            """;

    public static final String GET_LIKE_FILMS_QUERY = """
            SELECT film_id FROM film_likes WHERE user_id = ?;
            """;

    public static final String QUERY_FIND_FILMS_BY_DIRECTOR = """
                SELECT DISTINCT f.id AS film_id, f.name, f.description, f.release_date,
                       f.duration, mpa.id AS mpa_id, mpa.name AS mpa_name,
                       dir.id AS dir_id, dir.name AS dir_name,
                       (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                       (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                        FROM films_genres fg
                        JOIN genres gt ON gt.id = fg.genre_id
                        WHERE fg.film_id = f.id) AS genres
                FROM films f
                JOIN mpa ON f.mpa_id = mpa.id
                INNER JOIN directors dir ON f.director_id = dir.id
                WHERE LOWER(dir.name) LIKE '%' || LOWER(?) || '%';
            """;

    public static final String QUERY_FIND_FILMS_BY_TITLE = """
                SELECT f.id AS film_id, f.name, f.description, f.release_date,
                       f.duration, mpa.id AS mpa_id, mpa.name AS mpa_name,
                       dir.id AS dir_id, dir.name AS dir_name,
                       (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                       (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                        FROM films_genres fg
                        JOIN genres gt ON gt.id = fg.genre_id
                        WHERE fg.film_id = f.id) AS genres
                FROM films f
                JOIN mpa ON f.mpa_id = mpa.id
                LEFT JOIN directors dir ON f.director_id = dir.id
                WHERE LOWER(f.name) LIKE '%' || LOWER(?) || '%';
            """;

    public static final String QUERY_FIND_COMMON_FILMS = """
            SELECT f.id AS film_id, f.name, f.description,
                       f.release_date, f.duration, mpa.id AS mpa_id,
                       mpa.name AS mpa_name, dir.id AS dir_id, dir.name AS dir_name,
                       (SELECT COUNT(*) FROM film_likes l WHERE l.film_id = f.id) AS likes,
                       (SELECT STRING_AGG(CONCAT(fg.genre_id, ':', gt.name), ',' ORDER BY fg.genre_id)
                         FROM films_genres fg
                         JOIN genres gt ON gt.id = fg.genre_id
                         WHERE fg.film_id = f.id) AS genres
            FROM films f
            JOIN mpa ON f.mpa_id = mpa.id
            LEFT JOIN directors dir ON f.director_id = dir.id
            JOIN (
                SELECT uf.film_id, COUNT(uf.film_id) AS like_count
                FROM film_likes uf
                JOIN film_likes ff ON uf.film_id = ff.film_id
                WHERE uf.user_id = ? AND ff.user_id = ?
                GROUP BY uf.film_id
                ORDER BY COUNT(uf.film_id) DESC
            ) AS common_films ON f.id = common_films.film_id
            ORDER BY common_films.like_count DESC;
            """;
}

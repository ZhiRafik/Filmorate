package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public class FilmRepository extends BaseRepository<Film> {

    private static final String FIND_ALL_QUERY = "SELECT * FROM films";
    private static final String UPDATE_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                                                "duration = ?, genre_id = ?, mpa_id = ?";
    private static final String INSERT_QUERY = "INSERT INTO films(name, description, release_date, duration, "  +
                                                "genre_id, mpa_id) VALUES (?, ?, ?, ?, ?, ?) returning id";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM films WHERE id = ?";
    private static final String DELETE_BY_ID_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String ADD_LIKE_BY_ID = "INSERT INTO likes(user_id, film_id) VALUES (?, ?) " +
                                                 "ON CONFLICT DO NOTHING";
    private static final String FIND_LIKE_BY_ID = "SELECT COUNT(*) FROM likes WHERE user_id = ? AND filmd_id = ?";
    private static final String REMOVE_LIKE_BY_ID = "DELETE FROM likes WHERE user_id = ? AND film_id = ?";
    private static final String GET_MOST_POPULAR_FILMS = "SELECT film_id, COUNT (user_id) AS like_count " +
                                                        "FROM likes " +
                                                        "GROUP BY film_id " +
                                                        "ORDER BY like_count DESC " +
                                                        "LIMIT ?";

    public FilmRepository(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    public List<Film> findAll() {
        return findMany(FIND_ALL_QUERY);
    }

    public Optional<Film> findById(long filmId) {
        return findOne(FIND_BY_ID_QUERY, filmId);
    }

    public void delete(Film film) {
        delete(DELETE_BY_ID_QUERY, film.getId());
    }

    public boolean addLike(Long userId, Long filmId) {
        int rowsAffected = jdbc.update(ADD_LIKE_BY_ID, userId, filmId);
        return rowsAffected > 0;
    }

    public boolean checkLike(Long userId, Long filmId) {
        Integer count = jdbc.queryForObject(FIND_LIKE_BY_ID, Integer.class, userId, filmId);
        return count > 0;
    }

    public boolean removeLike(Long userId, Long filmId) {
        int rowsAffected = jdbc.update(REMOVE_LIKE_BY_ID, userId, filmId);
        return rowsAffected != 0;
    }

    public Film update(Film film) {
        update (
                UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenre_id(),
                film.getMpa_id()
        );
        return film;
    }

    public Film addFilm(Film film) {
        long id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getGenre_id(),
                film.getMpa_id()
        );
        film.setId(id);
        return film;
    }

    public List<Film> getMostPopularFilms(int n) {
        return jdbc.query(GET_MOST_POPULAR_FILMS, new Object[]{n}, new FilmRowMapper());
    }
}

package ru.yandex.practicum.filmorate.dal;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.enums.Genre;

import java.util.Collection;
import java.util.Optional;

@Repository
public class GenreRepository extends BaseRepository<Genre> {

    private static final String GET_GENRES = "SELECT * FROM genres";
    private static final String GET_GENRE_BY_ID = "SELECT * FROM genres WHERE id = ?";

    public GenreRepository(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    public Collection<Genre> getGenres() {
        return findMany(GET_GENRES);
    }

    public Optional<Genre> getGenre(int id) {
        return findOne(GET_GENRE_BY_ID, id);
    }
}

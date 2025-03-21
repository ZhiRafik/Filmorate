package ru.yandex.practicum.filmorate.dal.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.CatalogDao;
import ru.yandex.practicum.filmorate.dal.base.JdbcUtil;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.dal.impl.query.CatalogQuery.*;

@Component
public class CatalogDaoImpl implements CatalogDao {

    private static final String CATALOG_EXCEPTION = "Каталога с таким идентификатором не существует";

    private final JdbcTemplate jdbc;

    private final NamedParameterJdbcTemplate namedJdbcTemplate;

    public CatalogDaoImpl(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
        this.namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbc);
    }

    @Override
    public List<Genre> getGenres() {
        return jdbc.query(GET_GENRES_QUERY, this::mapGenre);
    }

    @Override
    public Genre getGenre(Long genreId) {
        return jdbc.queryForObject(GET_GENRE_QUERY, this::mapGenre, genreId);
    }

    @Override
    public void checkGenresExistOrThrow(List<Long> genreIds) {
        if (genreIds.isEmpty()) {
            return;
        }
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        parameters.addValue("genreIds", genreIds);
        Integer count = namedJdbcTemplate.queryForObject(GET_COUNT_GENRES, parameters, Integer.class);
        if (count == null || count != genreIds.size()) {
            throw new NotFoundException(CATALOG_EXCEPTION);
        }
    }

    @Override
    public List<Mpa> getAllMpa() {
        return jdbc.query(GET_ALL_MPA_QUERY, this::mapMpa);
    }

    @Override
    public Mpa getMpa(Long mpaId) {
        return jdbc.queryForObject(GET_MPA_QUERY, this::mapMpa, mpaId);
    }

    @Override
    public void checkMpaExistOrThrow(long mpaId) {
        int count = jdbc.queryForObject(GET_COUNT_MPA, Integer.class, mpaId);
        if (count == 0) {
            throw new NotFoundException(CATALOG_EXCEPTION);
        }
    }

    @Override
    public Collection<Director> getDirectors() {
        return jdbc.query(GET_DIRECTORS_QUERY, this::mapDirector);
    }

    @Override
    public Director getDirector(long directorId) {
        return jdbc.queryForObject(GET_DIRECTOR_QUERY, this::mapDirector, directorId);
    }

    @Override
    public long createDirector(Director director) {
        return JdbcUtil.insert(jdbc, INSERT_DIRECTOR_QUERY, director.getName());
    }

    @Override
    public long updateDirector(Director director) {
        JdbcUtil.update(jdbc, UPDATE_DIRECTOR_QUERY, director.getName(), director.getId());
        return director.getId();
    }

    @Override
    public void deleteDirectorById(long directorId) {
        JdbcUtil.remove(jdbc, DELETE_DIRECTOR_QUERY, directorId);
    }

    @Override
    public void checkDirectorExistOrThrow(long directorId) {
        int count = jdbc.queryForObject(GET_COUNT_DIRECTORS, Integer.class, directorId);
        if (count == 0) {
            throw new NotFoundException(CATALOG_EXCEPTION);
        }
    }

    private Genre mapGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }

    private Mpa mapMpa(ResultSet rs, int rowNum) throws SQLException {
        return Mpa.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }

    private Director mapDirector(ResultSet rs, int rowNum) throws SQLException {
        return Director.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .build();
    }
}

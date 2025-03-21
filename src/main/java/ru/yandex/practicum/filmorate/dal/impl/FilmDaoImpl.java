package ru.yandex.practicum.filmorate.dal.impl;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.dal.FilmDao;
import ru.yandex.practicum.filmorate.dal.base.BaseDao;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.yandex.practicum.filmorate.dal.impl.query.FilmQuery.*;

@Primary
@Component
public class FilmDaoImpl extends BaseDao<Film> implements FilmDao {

    public FilmDaoImpl(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Long insert(Film film) {
        Long directorId = film.getDirector() == null ? null : film.getDirector().getId();
        Long id = insert(CREATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                directorId);
        insertGenres(id, film.getGenres());
        return id;
    }

    @Override
    @Transactional
    public Long update(Film film) {
        Long directorId = film.getDirector() == null ? null : film.getDirector().getId();
        update(UPDATE_FILM_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                directorId,
                film.getId());
        updateGenres(film.getId(), film.getGenres());
        return film.getId();
    }

    @Override
    public List<Film> getFilms() {
        return findMany(GET_FILMS_QUERY);
    }

    @Override
    public Film getFilm(long filmId) {
        return findOne(GET_FILM_QUERY, filmId);
    }

    @Override
    public boolean delete(long filmId) {
        return remove(DELETE_FILM_BY_ID_QUERY, filmId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        update(ADD_LIKE_QUERY, filmId, userId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        remove(REMOVE_LIKE_QUERY, filmId, userId);
    }

    @Override
    public List<Long> getLikeFilms(long userId) {
        return jdbc.queryForList(GET_LIKE_FILMS_QUERY, Long.class, userId);
    }

    @Override
    public Collection<Film> findPopularFilms(long count) {
        return findMany(FIND_POPULAR_FILMS_QUERY, count);
    }

    @Override
    public Collection<Film> findPopularFilms(long count, long genreId, long year) {
        return findMany(FIND_POPULAR_FILMS_GENRE_YEAR_QUERY, genreId, year, count);
    }

    @Override
    public Collection<Film> findPopularFilmsWithoutYear(long count, long genreId) {
        return findMany(FIND_POPULAR_FILMS_GENRE_QUERY, genreId, count);
    }

    @Override
    public Collection<Film> findPopularFilmsWithoutGenre(long count, long year) {
        return findMany(FIND_POPULAR_FILMS_YEAR_QUERY, year, count);
    }

    @Override
    public Collection<Film> findRecommendedFilms(long idToRecommend, long idWithSharedLikes) {
        return findMany(FIND_RECOMMENDED_FILMS_QUERY, idToRecommend, idWithSharedLikes);
    }

    @Override
    public Collection<Film> findFilmWithDirectorsOnYear(long directorId) {
        return findMany(DIRECTOR_ON_YEAR_QUERY, directorId);
    }

    @Override
    public Collection<Film> findFilmWithDirectorsOnLikes(long directorId) {
        return findMany(DIRECTOR_FIND_ON_LIKES_QUERY, directorId);
    }

    @Override
    public Collection<Film> findFilmsByDirector(String query) {
        return findMany(QUERY_FIND_FILMS_BY_DIRECTOR, query);
    }

    @Override
    public Collection<Film> findFilmsByTitle(String query) {
        return findMany(QUERY_FIND_FILMS_BY_TITLE, query);
    }

    @Override
    public Collection<Film> findCommonFilms(long userId, long friendId) {
        return findMany(QUERY_FIND_COMMON_FILMS, userId, friendId);
    }

    private void updateGenres(Long filmId, Collection<Genre> genres) {
        update(DELETE_FILM_GENRES_QUERY, filmId);
        insertGenres(filmId, genres);
    }

    private void insertGenres(Long filmId, Collection<Genre> genres) {
        if (genres != null && !genres.isEmpty()) {
            List<Object[]> batchArgs = new ArrayList<>();
            for (Genre genre : genres) {
                batchArgs.add(new Object[]{filmId, genre.getId()});
            }
            jdbc.batchUpdate(INSERT_FILM_GENRE_QUERY, batchArgs);
        }
    }

    @Override
    public void checkFilmExistOrThrow(long filmId) {
        int count = jdbc.queryForObject(COUNT_FILM_QUERY, Integer.class, filmId);
        if (count == 0) {
            throw new NotFoundException("Фильм с таким идентификатором не существует");
        }
    }
}

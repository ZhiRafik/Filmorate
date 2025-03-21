package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;

public interface FilmDao {

    Long insert(Film film);

    Long update(Film film);

    List<Film> getFilms();

    Film getFilm(long filmId);

    boolean delete(long filmId);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Long> getLikeFilms(long userId);

    Collection<Film> findPopularFilms(long count);

    Collection<Film> findPopularFilms(long count, long genreId, long year);

    Collection<Film> findPopularFilmsWithoutYear(long count, long genreId);

    Collection<Film> findPopularFilmsWithoutGenre(long count, long year);

    Collection<Film> findRecommendedFilms(long idToRecommend, long idWithSharedLikes);

    Collection<Film> findFilmWithDirectorsOnYear(long directorId);

    Collection<Film> findFilmWithDirectorsOnLikes(long directorId);

    Collection<Film> findFilmsByDirector(String query);

    Collection<Film> findFilmsByTitle(String query);

    Collection<Film> findCommonFilms(long userId, long friendsId);

    void checkFilmExistOrThrow(long filmId);
}

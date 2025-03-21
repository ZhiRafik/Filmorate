package ru.yandex.practicum.filmorate.dal;

import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;
import java.util.List;

public interface CatalogDao {

    List<Genre> getGenres();

    Genre getGenre(Long genreId);

    void checkGenresExistOrThrow(List<Long> genreIds);

    List<Mpa> getAllMpa();

    Mpa getMpa(Long mpaId);

    void checkMpaExistOrThrow(long mpaId);

    Collection<Director> getDirectors();

    Director getDirector(long directorId);

    long createDirector(Director director);

    long updateDirector(Director director);

    void deleteDirectorById(long directorId);

    void checkDirectorExistOrThrow(long directorId);
}

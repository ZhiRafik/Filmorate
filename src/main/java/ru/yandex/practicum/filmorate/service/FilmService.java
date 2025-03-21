package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.CatalogDao;
import ru.yandex.practicum.filmorate.dal.EventDao;
import ru.yandex.practicum.filmorate.dal.FilmDao;
import ru.yandex.practicum.filmorate.dal.UserDao;
import ru.yandex.practicum.filmorate.mapper.dto.FilmMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.SearchType;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FilmService {

    private final FilmDao filmDao;
    private final UserDao userDao;
    private final CatalogDao catalogDao;
    private final EventDao eventDao;

    public FilmDto addFilm(FilmDto dto) {
        Film film = FilmMapper.mapToModel(dto);
        checkCatalogsExistOrThrow(film);
        Long id = filmDao.insert(film);
        dto.setId(id);
        return dto;
    }

    public FilmDto updateFilm(FilmDto dto) {
        Film newFilm = FilmMapper.mapToModel(dto);
        checkCatalogsExistOrThrow(newFilm);
        filmDao.checkFilmExistOrThrow(newFilm.getId());
        Film oldFilm = filmDao.getFilm(newFilm.getId());
        Film result = update(oldFilm, newFilm);
        filmDao.update(result);
        return FilmMapper.mapToDto(result);
    }

    public Collection<FilmDto> getFilms() {
        return filmDao.getFilms().stream()
                .map(FilmMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public FilmDto getFilm(long filmId) {
        filmDao.checkFilmExistOrThrow(filmId);
        Film film = filmDao.getFilm(filmId);
        return FilmMapper.mapToDto(film);
    }

    public Map<String, String> deleteFilm(long filmId) {
        filmDao.checkFilmExistOrThrow(filmId);
        filmDao.delete(filmId);
        return Map.of("description", String.format("Фильм с идентификатором: %d успешно удален.", filmId));
    }

    public void like(long filmId, long userId) {
        userDao.checkUserExistsOrThrow(userId);
        filmDao.checkFilmExistOrThrow(filmId);
        filmDao.removeLike(filmId, userId);
        filmDao.addLike(filmId, userId);
        eventDao.insert(userId, "LIKE", "ADD", filmId);
    }

    public void removeLike(long filmId, long userId) {
        userDao.checkUserExistsOrThrow(userId);
        filmDao.checkFilmExistOrThrow(filmId);
        filmDao.removeLike(filmId, userId);
        eventDao.insert(userId, "LIKE", "REMOVE", filmId);
    }

    public Collection<FilmDto> findPopularFilms(Long count, Long genreId, Long year) {
        Collection<Film> films;
        if (genreId == null && year == null) {
            films = filmDao.findPopularFilms(count);
        } else if (genreId != null && year == null) {
            catalogDao.checkGenresExistOrThrow(List.of(genreId));
            films = filmDao.findPopularFilmsWithoutYear(count, genreId);
        } else if (genreId == null) {
            films = filmDao.findPopularFilmsWithoutGenre(count, year);
        } else {
            catalogDao.checkGenresExistOrThrow(List.of(genreId));
            films = filmDao.findPopularFilms(count, genreId, year);
        }
        return FilmMapper.mapToDto(films);
    }

    public List<FilmDto> findFilmWithDirectors(long directorId, String type) {
        catalogDao.checkDirectorExistOrThrow(directorId);
        Collection<Film> films;
        if (type.equals("likes")) {
            films = filmDao.findFilmWithDirectorsOnLikes(directorId);
        } else if (type.equals("year")) {
            films = filmDao.findFilmWithDirectorsOnYear(directorId);
        } else {
            films = List.of();
        }
        return FilmMapper.mapToDto(films);
    }

    public Collection<FilmDto> searchFilms(String query, List<String> by) {
        Collection<SearchType> types = by.stream().map(SearchType::fromValue).toList();
        List<Film> result = new ArrayList<>();
        for (SearchType type : types) {
            switch (type) {
                case DIRECTOR -> result.addAll(filmDao.findFilmsByDirector(query));
                case TITLE -> result.addAll(filmDao.findFilmsByTitle(query));
                default -> throw new IllegalArgumentException("Указан неподдерживаемый параметр: " + type.getValue());
            }
        }

        return FilmMapper.mapToDto(result.stream()
                .sorted(Comparator.comparing(Film::getId).reversed())
                .collect(Collectors.toList()));
    }

    private List<Film> sortFilmsOnGetId(List<Film> films) {
        return films.stream().sorted(Comparator.comparing(Film::getId)).collect(Collectors.toList());
    }

    public Collection<FilmDto> searchCommonFilms(Long userId, Long friendId) {
        userDao.checkUserExistsOrThrow(userId);
        userDao.checkUserExistsOrThrow(friendId);
        Collection<Film> films = filmDao.findCommonFilms(userId, friendId);
        return FilmMapper.mapToDto(films);
    }

    private void checkCatalogsExistOrThrow(Film film) {
        List<Long> genreIds = film.getGenres().stream()
                .map(Genre::getId)
                .toList();
        catalogDao.checkGenresExistOrThrow(genreIds);
        catalogDao.checkMpaExistOrThrow(film.getMpa().getId());
        Director director = film.getDirector();
        if (director != null) {
            catalogDao.checkDirectorExistOrThrow(director.getId());
        }
    }

    private Film update(Film originalFilm, Film newFilm) {
        return Film.builder()
                .id(newFilm.getId())
                .name(newFilm.getName() == null ? originalFilm.getName() : newFilm.getName())
                .description(newFilm.getDescription() == null ? originalFilm.getDescription() : newFilm.getDescription())
                .duration(newFilm.getDuration() == null ? originalFilm.getDuration() : newFilm.getDuration())
                .releaseDate(newFilm.getReleaseDate() == null ? originalFilm.getReleaseDate() : newFilm.getReleaseDate())
                .likeCount(originalFilm.getLikeCount())
                .mpa(newFilm.getMpa())
                .genres(newFilm.getGenres().stream().sorted(Comparator.comparing(Genre::getId)).toList())
                // .director(newFilm.getDirector() == null ? originalFilm.getDirector() : newFilm.getDirector())
                .director(newFilm.getDirector())
                .build();
    }
}

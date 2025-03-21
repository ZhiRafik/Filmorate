package ru.yandex.practicum.filmorate.mapper.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilmMapper {

    public static List<FilmDto> mapToDto(Collection<Film> films) {
        return films.stream()
                .map(FilmMapper::mapToDto)
                .toList();
    }

    public static Film mapToModel(FilmDto dto) {
        return Film.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .releaseDate(dto.getReleaseDate())
                .duration(dto.getDuration())
                .mpa(CatalogMapper.catalogToMpa(dto.getMpa()))
                .genres(CatalogMapper.catalogsToGenres(dto.getGenres()))
                .director(
                        dto.getDirectors() == null || dto.getDirectors().isEmpty() ?
                                null : CatalogMapper.catalogToDirector(dto.getDirectors().getFirst())
                )
                .build();
    }

    public static FilmDto mapToDto(Film film) {
        return FilmDto.builder()
                .id(film.getId())
                .name(film.getName())
                .description(film.getDescription())
                .releaseDate(film.getReleaseDate())
                .duration(film.getDuration())
                .mpa(CatalogMapper.mpaToCatalog(film.getMpa()))
                .genres(CatalogMapper.genresToCatalogs(film.getGenres()))
                .directors(
                        film.getDirector() == null ?
                                Collections.emptyList() : List.of(CatalogMapper.directorToCatalog(film.getDirector()))
                )
                .build();
    }
}
